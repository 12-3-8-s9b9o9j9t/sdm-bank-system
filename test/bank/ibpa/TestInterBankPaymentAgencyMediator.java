package bank.ibpa;

import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import bank.Bank;
import bank.exception.BankAlreadyRegisteredAtIbpaException;
import bank.exception.InvalidBankException;
import bank.transaction.transfer.TransferCommand;

public class TestInterBankPaymentAgencyMediator {
    
    private Bank mockBank = null;
    private Bank mockBank2 = null;
    private TransferCommand mockTransfer = null;
    private TransferCommand mockTransfer2 = null;
    private InterBankPaymentAgencyMediator ibpa = null;
    private MockedConstruction<Random> mockedRandom = null;

    @Before
    public void setUp() throws Exception {
        mockTransfer = mock(TransferCommand.class);
        when(mockTransfer.getReceivingBankID()).thenReturn("TESTBANK0");
        mockTransfer2 = mock(TransferCommand.class);
        when(mockTransfer2.getReceivingBankID()).thenReturn("OTHERBANK1");
        mockBank = mock(Bank.class);
        when(mockBank.getName()).thenReturn("TestBANK");
        when(mockBank.getPendingTransfers(anyString())).thenReturn(List.of(mockTransfer2));
        mockBank2 = mock(Bank.class);
        when(mockBank2.getName()).thenReturn("OtherBANK");
        when(mockBank2.getPendingTransfers(anyString())).thenReturn(List.of(mockTransfer));
        ibpa = new InterBankPaymentAgencyMediator("SWIFT");
        mockedRandom = mockConstruction(Random.class, (mock, context) -> {
            when(mock.nextInt()).thenReturn(0);
        });
    }

    @After
    public void tearDown() {
        mockBank = null;
        mockBank2 = null;
        mockTransfer = null;
        mockTransfer2 = null;
        ibpa = null;
        mockedRandom.close();
    }

    @Test
    public void test_notify_register() throws Exception {
        ibpa.notify(mockBank, "register");
        verify(mockBank).addIbpa("TESTBANK0", ibpa);
    }

    @Test(expected = BankAlreadyRegisteredAtIbpaException.class)
    public void test_notify_register_already_registered() throws Exception {
        ibpa.notify(mockBank, "register");
        try {
            ibpa.notify(mockBank, "register");
        } finally {
            verify(mockBank).addIbpa("TESTBANK0", ibpa);
        }
    }

    @Test(expected = InvalidBankException.class)
    public void notify_invalid_bank() throws Exception {
        ibpa.notify(mockBank, "test");
    }

    @Test
    public void test_transfer() throws Exception {
        ibpa.notify(mockBank, "register");
        ibpa.notify(mockBank2, "register");
        ibpa.transfer();
        verify(mockBank).addPendingTransfer(mockTransfer);
        verify(mockTransfer, never()).execute();
        verify(mockBank2, never()).addPendingTransfer(any(TransferCommand.class));
        verify(mockTransfer2).execute();
        verify(mockBank).clearPendingTransfers("SWIFT");
        verify(mockBank2).clearPendingTransfers("SWIFT");
    }


}
