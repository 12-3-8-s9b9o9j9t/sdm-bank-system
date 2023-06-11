package bank.transaction;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.exception.InvalidTransactionException;
import bank.product.ISuppliable;
import bank.reporter.IVisitor;
import bank.product.AProduct;

public class TestSupplyProductCommand {
    
    private AProduct mockSupplied = null;
    private SupplyProductCommand command = null;

    @Before
    public void setUp() {
        mockSupplied = mock(AProduct.class, withSettings().extraInterfaces(ISuppliable.class));
        when(mockSupplied.getId()).thenReturn("ID");
        command = new SupplyProductCommand((ISuppliable)mockSupplied, 1000.0);
    }

    @After
    public void tearDown() {
        command = null;
        mockSupplied = null;
    }

    @Test
    public void test_execute_success() throws Exception {
        boolean success = command.execute();
        assertTrue(success);
        verify((ISuppliable)mockSupplied).supply(anyDouble());
        assertFalse(command.getDescription().endsWith(": Failed"));
        verify(mockSupplied).log(command);
    }

    @Test
    public void test_execute_failure() throws Exception {
        doAnswer(invocation -> {
            throw new InvalidTransactionException("test", "test");
        }).when((ISuppliable)mockSupplied).supply(anyDouble());
        boolean success = command.execute();
        assertFalse(success);
        verify((ISuppliable)mockSupplied).supply(anyDouble());
        assertTrue(command.getDescription().endsWith(": Failed"));
        verify(mockSupplied).log(command);
    }

    @Test
    public void test_getValue() {
        double value = command.getValue();
        assertEquals(1000.0, value, 0.0);
    }

}
