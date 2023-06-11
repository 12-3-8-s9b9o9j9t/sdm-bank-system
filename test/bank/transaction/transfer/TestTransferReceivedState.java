package bank.transaction.transfer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestTransferReceivedState {

    private TransferCommand mockContext = null;
    private TransferReceivedState state = null;

    @Before
    public void setUp() {
        mockContext = mock(TransferCommand.class);
        when(mockContext.getAmount()).thenReturn(100.0);
        state = new TransferReceivedState(mockContext);
    }

    @After
    public void tearDown() {
        mockContext = null;
        state = null;
    }

    @Test
    public void test_execute() {
        boolean result = state.execute();
        assertFalse(result);
    }

    @Test
    public void test_getValue() {
        double result = state.getValue();
        assertEquals(100.0, result, 0.0);
    }
}
