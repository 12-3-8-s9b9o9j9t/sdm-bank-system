package bank.transaction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.product.AProduct;

public class TestCalculateInterestCommand {
    
    private AProduct mockProduct = null;
    private CalculateInterestCommand command = null;

    @Before
    public void setUp() {
        mockProduct = mock(AProduct.class);
        when(mockProduct.getId()).thenReturn("ID");
        when(mockProduct.calculateInterest()).thenReturn(100.0);
        command = new CalculateInterestCommand(mockProduct);
    }

    @After
    public void tearDown() {
        command = null;
        mockProduct = null;
    }

    @Test
    public void test_execute() {
        boolean success = command.execute();
        assertTrue(success);
        verify(mockProduct).calculateInterest();
        verify(mockProduct).log(command);
    }

    @Test
    public void test_getValue_beforeExecution() {
        double value = command.getValue();
        assertEquals(0.0, value, 0.0);
    }

    @Test
    public void test_getValue_afterExecution() {
        command.execute();
        double value = command.getValue();
        assertEquals(100.0, value, 0.0);
    }

}
