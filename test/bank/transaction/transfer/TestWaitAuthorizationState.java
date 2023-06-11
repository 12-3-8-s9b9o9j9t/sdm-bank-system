package bank.transaction.transfer;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import bank.Bank;

public class TestWaitAuthorizationState {
    
    private TransferCommand mockContext = null;
    private Bank mockSenderBank = null;
    private WaitAuthorizationState state = null;
    private LocalDateTime fixedTime = null;
    private MockedStatic<LocalDateTime> mockStatic = null;
    private MockedConstruction<WaitReceivingAccountState> waitReceivingAccountState = null;

    @Before
    public void setUp() {
        mockContext = mock(TransferCommand.class);
        mockSenderBank = mock(Bank.class);
        fixedTime = LocalDateTime.of(2023, 6, 11, 15, 15, 0);
        mockStatic = mockStatic(LocalDateTime.class);
        when(LocalDateTime.now()).thenReturn(fixedTime);
        state = new WaitAuthorizationState(mockContext);
        waitReceivingAccountState = mockConstruction(WaitReceivingAccountState.class);
        when(mockContext.getSenderBank()).thenReturn(mockSenderBank);
        when(mockContext.changeState(any(ATransferState.class))).thenReturn(mockContext);
    }

    @After
    public void tearDown() {
        mockContext = null;
        mockSenderBank = null;
        state = null;
        fixedTime = null;
        mockStatic.close();
        waitReceivingAccountState.close();
    }

    @Test
    public void test_execute_success() throws Exception {
        Field f = state.getClass().getDeclaredField("time");
        f.setAccessible(true);
        LocalDateTime time = (LocalDateTime) f.get(state);
        f.set(state, time.minusMinutes(3)); // simulate 3 minutes passed
        boolean result = state.execute();
        assertTrue(result);
        assertEquals(1, waitReceivingAccountState.constructed().size());
        verify(mockContext).changeState(waitReceivingAccountState.constructed().get(0));
        verify(mockSenderBank).addPendingTransfer(mockContext);
    }

    @Test
    public void test_execute_failure() throws Exception {
        Field f = state.getClass().getDeclaredField("time");
        f.setAccessible(true);
        LocalDateTime time = (LocalDateTime) f.get(state);
        f.set(state, time.minusMinutes(6)); // simulate 6 minutes passed
        boolean result = state.execute();
        assertFalse(result);
        assertEquals(0, waitReceivingAccountState.constructed().size());
        verify(mockContext, never()).changeState(any(ATransferState.class));
        verify(mockSenderBank, never()).addPendingTransfer(mockContext);    }

    @Test
    public void test_getValue() {
        double value = state.getValue();
        assertEquals(0.0, value, 0.0);
    }
}
