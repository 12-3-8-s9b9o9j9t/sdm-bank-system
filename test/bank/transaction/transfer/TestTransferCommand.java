package bank.transaction.transfer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import bank.Bank;
import bank.Customer;
import bank.product.account.AAccount;

public class TestTransferCommand {
    
    private Bank mockBank = null;
    private Customer mockCustomer = null;
    private AAccount mockSendingAccount = null;
    private ATransferState mockState = null;
    private TransferCommand commandNoIbpa = null;
    private TransferCommand commandWithIbpa = null;
    private MockedConstruction<VerifyAmountState> verifyAmountState = null;

    @Before
    public void setUp() {
        mockBank = mock(Bank.class);
        mockCustomer = mock(Customer.class);
        mockSendingAccount = mock(AAccount.class);
        when(mockSendingAccount.getId()).thenReturn("ID");
        mockState = mock(ATransferState.class);
        verifyAmountState = mockConstruction(VerifyAmountState.class);
        commandNoIbpa = new TransferCommand(mockBank, mockCustomer, mockSendingAccount, "ID2", 100.0);
        commandWithIbpa = new TransferCommand(mockBank, mockCustomer, mockSendingAccount, "ID2", "TESTBANK0", "SWIFT", 100.0);
    }

    @After
    public void tearDown() {
        mockBank = null;
        mockCustomer = null;
        mockSendingAccount = null;
        mockState = null;
        commandNoIbpa = null;
        commandWithIbpa = null;
        verifyAmountState.close();
    }

    @Test
    public void test_stateInitialisation_noIbpa() throws Exception {
        Field f = commandNoIbpa.getClass().getDeclaredField("state");
        f.setAccessible(true);
        ATransferState state = (ATransferState) f.get(commandNoIbpa);
        assertEquals(state, verifyAmountState.constructed().get(0));
    }

    @Test
    public void test_stateInitialisation_withIbpa() throws Exception {
        Field f = commandWithIbpa.getClass().getDeclaredField("state");
        f.setAccessible(true);
        ATransferState state = (ATransferState) f.get(commandWithIbpa);
        assertEquals(state, verifyAmountState.constructed().get(1));
    }

    @Test
    public void test_execute_noIbpa_success() throws Exception {
        Field f = commandNoIbpa.getClass().getDeclaredField("state");
        f.setAccessible(true);
        f.set(commandNoIbpa, mockState);
        when(mockState.execute()).thenReturn(true);
        commandNoIbpa.execute();
        verify(mockState).execute();
        verify(mockSendingAccount, never()).log(commandNoIbpa);
    }

    @Test
    public void test_execute_withIbpa_success() throws Exception {
        Field f = commandWithIbpa.getClass().getDeclaredField("state");
        f.setAccessible(true);
        f.set(commandWithIbpa, mockState);
        when(mockState.execute()).thenReturn(true);
        commandWithIbpa.execute();
        verify(mockState).execute();
        verify(mockSendingAccount, never()).log(commandWithIbpa);
    }

    @Test
    public void test_execute_noIbpa_failure() throws Exception {
        Field f = commandNoIbpa.getClass().getDeclaredField("state");
        f.setAccessible(true);
        f.set(commandNoIbpa, mockState);
        when(mockState.execute()).thenReturn(false);
        commandNoIbpa.execute();
        verify(mockState).execute();
        verify(mockSendingAccount).log(commandNoIbpa);
        assertTrue(commandNoIbpa.getDescription().endsWith(": Failed"));
    }

    @Test
    public void test_execute_withIbpa_failure() throws Exception {
        Field f = commandWithIbpa.getClass().getDeclaredField("state");
        f.setAccessible(true);
        f.set(commandWithIbpa, mockState);
        when(mockState.execute()).thenReturn(false);
        commandWithIbpa.execute();
        verify(mockState).execute();
        verify(mockSendingAccount).log(commandWithIbpa);
        assertTrue(commandWithIbpa.getDescription().endsWith(": Failed"));
    }

    @Test
    public void test_getValue_noIbpa() throws Exception {
        Field f = commandNoIbpa.getClass().getDeclaredField("state");
        f.setAccessible(true);
        f.set(commandNoIbpa, mockState);
        commandNoIbpa.getValue();
        verify(mockState).getValue();
    }

    @Test
    public void test_getValue_withIbpa() throws Exception {
        Field f = commandWithIbpa.getClass().getDeclaredField("state");
        f.setAccessible(true);
        f.set(commandWithIbpa, mockState);
        commandWithIbpa.getValue();
        verify(mockState).getValue();
    }

}
