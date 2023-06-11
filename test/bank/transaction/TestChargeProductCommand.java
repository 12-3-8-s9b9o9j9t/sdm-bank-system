package bank.transaction;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.exception.InvalidTransactionException;
import bank.product.IChargeable;
import bank.product.Product;

public class TestChargeProductCommand {
    
    private Product mockCharged = null;
    private ChargeProductCommand command = null;

    @Before
    public void setUp() {
        mockCharged = mock(Product.class, withSettings().extraInterfaces(IChargeable.class));
        when(mockCharged.getId()).thenReturn("ID");
        command = new ChargeProductCommand((IChargeable)mockCharged, 1000.0);
    }

    @After
    public void tearDown() {
        command = null;
        mockCharged = null;
    }

    @Test
    public void test_execute_success() throws Exception {
        boolean success = command.execute();
        assertTrue(success);
        verify((IChargeable)mockCharged).charge(anyDouble());
        assertFalse(command.getDescription().endsWith(": Failed"));
        verify(mockCharged).log(command);
    }

    @Test
    public void test_execute_failure() throws Exception {
        doAnswer(invocation -> {
            throw new InvalidTransactionException("test", "test");
        }).when((IChargeable)mockCharged).charge(anyDouble());
        boolean success = command.execute();
        assertFalse(success);
        verify((IChargeable)mockCharged).charge(anyDouble());
        assertTrue(command.getDescription().endsWith(": Failed"));
        verify(mockCharged).log(command);
    }

    @Test
    public void test_getValue() {
        double value = command.getValue();
        assertEquals(-1000.0, value, 0.0);
    }

}
