package bank.transaction.transfer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import bank.product.account.AAccount;

public class TestWaitReceivingAccountState {
    

    private TransferCommand mockContext = null;
    private TransferCommand mockContextCopy = null;
    private TransferCommand mockContextCopy2 = null;
    private AAccount mockReceivingAccount = null;
    private AAccount mockSendingAccount = null;
    private WaitReceivingAccountState state = null;
    private MockedConstruction<TransferSentState> transferSentState = null;
    private MockedConstruction<TransferReceivedState> transferReceivedState = null;

    @Before
    public void setUp() {
        mockContext = mock(TransferCommand.class);
        mockContextCopy = mock(TransferCommand.class);
        mockContextCopy2 = mock(TransferCommand.class);
        mockSendingAccount = mock(AAccount.class);
        state = new WaitReceivingAccountState(mockContext);
        when(mockContext.getReceivingAccountID()).thenReturn("ID");
        when(mockContext.getSendingAccount()).thenReturn(mockSendingAccount);
        when(mockContext.getAmount()).thenReturn(100.0);
        when(mockContext.copy()).thenReturn(mockContextCopy, mockContextCopy2);
        when(mockContextCopy.changeState(any(ATransferState.class))).thenReturn(mockContextCopy);
        when(mockContextCopy2.changeState(any(ATransferState.class))).thenReturn(mockContextCopy2);
        transferSentState = mockConstruction(TransferSentState.class);
        transferReceivedState = mockConstruction(TransferReceivedState.class);
    }

    @After
    public void tearDown() {
        mockContext = null;
        mockContextCopy = null;
        mockContextCopy2 = null;
        mockSendingAccount = null;
        mockReceivingAccount = null;
        state = null;
        transferSentState.close();
        transferReceivedState.close();
    }

    @Test
    public void test_execute_success() throws Exception {
        mockReceivingAccount = mock(AAccount.class);
        when(mockContext.getReceivingAccount()).thenReturn(mockReceivingAccount);
        when(mockReceivingAccount.getId()).thenReturn("ID");
        boolean result = state.execute();
        assertTrue(result);
        verify(mockSendingAccount).charge(100.0);
        verify(mockReceivingAccount).supply(100.0);
        verify(mockContext, times(2)).copy();
        assertEquals(1, transferSentState.constructed().size());
        verify(mockSendingAccount).log(mockContextCopy);
        verify(mockContextCopy).changeState(transferSentState.constructed().get(0));
        assertEquals(1, transferReceivedState.constructed().size());
        verify(mockReceivingAccount).log(mockContextCopy2);
        verify(mockContextCopy2).changeState(transferReceivedState.constructed().get(0));
        verify(mockSendingAccount, never()).log(mockContext);
        verify(mockSendingAccount, never()).log(mockContextCopy2);
        verify(mockReceivingAccount, never()).log(mockContext);
        verify(mockReceivingAccount, never()).log(mockContextCopy);
    }

    @Test
    public void test_execute_failure_receivingAccountNotfound() throws Exception {
        when(mockContext.getReceivingAccount()).thenReturn(mockReceivingAccount);
        boolean result = state.execute();
        assertFalse(result);
        verify(mockSendingAccount, never()).charge(anyDouble());
        verify(mockContext, never()).copy();
        verify(mockSendingAccount, never()).log(mockContext);
    }

    @Test
    public void test_execute_failure_receivingAccountNotMatching() throws Exception {
        mockReceivingAccount = mock(AAccount.class);
        when(mockContext.getReceivingAccount()).thenReturn(mockReceivingAccount);
        when(mockReceivingAccount.getId()).thenReturn("ID2");
        boolean result = state.execute();
        assertFalse(result);
        verify(mockSendingAccount, never()).charge(anyDouble());
        verify(mockReceivingAccount, never()).supply(anyDouble());
        verify(mockContext, never()).copy();
        verify(mockSendingAccount, never()).log(any(TransferCommand.class));
        verify(mockReceivingAccount, never()).log(any(TransferCommand.class));
    }

    @Test
    public void test_getValue() {
        double result = state.getValue();
        assertEquals(0.0, result, 0.0);
    }
}
