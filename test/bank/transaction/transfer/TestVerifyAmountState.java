package bank.transaction.transfer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import bank.Customer;
import bank.product.account.AAccount;

public class TestVerifyAmountState {
    
    private TransferCommand mockContext = null;
    private AAccount mockSendingAccount = null;
    private Customer mockSender = null;
    private VerifyAmountState state = null;
    private MockedConstruction<WaitAuthorizationState> waitAuthorizationState = null;

    @Before
    public void setUp() {
        mockContext = mock(TransferCommand.class);
        mockSendingAccount = mock(AAccount.class);
        mockSender = mock(Customer.class);
        state = new VerifyAmountState(mockContext);
        waitAuthorizationState = mockConstruction(WaitAuthorizationState.class);
        when(mockContext.getAmount()).thenReturn(100.0);
        when(mockContext.getSendingAccount()).thenReturn(mockSendingAccount);
        when(mockContext.getSender()).thenReturn(mockSender);
        when(mockContext.changeState(any(ATransferState.class))).thenReturn(mockContext);
    }

    @After
    public void tearDown() {
        mockContext = null;
        mockSendingAccount = null;
        mockSender = null;
        state = null;
        waitAuthorizationState.close();
    }

    @Test
    public void test_execute_success() {
        when(mockSendingAccount.getBalance()).thenReturn(1000.0);
        boolean success = state.execute();
        assertTrue(success);
        assertEquals(1, waitAuthorizationState.constructed().size());
        verify(mockContext).changeState(waitAuthorizationState.constructed().get(0));
        verify(mockSender).addTransferToAuthorize(mockContext);
    }

    @Test
    public void test_execute_failure() {
        when(mockSendingAccount.getBalance()).thenReturn(50.0);
        boolean success = state.execute();
        assertFalse(success);
        assertEquals(0, waitAuthorizationState.constructed().size());
        verify(mockContext, never()).changeState(any(ATransferState.class));
        verify(mockSender, never()).addTransferToAuthorize(any(TransferCommand.class));
    }

    @Test
    public void test_getValue() {
        double value = state.getValue();
        assertEquals(0, value, 0.001);
    }
}
