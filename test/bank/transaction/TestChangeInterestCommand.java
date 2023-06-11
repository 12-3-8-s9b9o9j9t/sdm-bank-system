package bank.transaction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.interest.AInterestStrategy;
import bank.product.Product;

public class TestChangeInterestCommand {
    
    private Product mockProduct = null;
    private AInterestStrategy mockStrategy = null;
    private ChangeInterestCommand command = null;

    @Before
    public void setUp() {
        mockProduct = mock(Product.class);
        when(mockProduct.getId()).thenReturn("ID");
        mockStrategy = mock(AInterestStrategy.class);
        when(mockStrategy.getName()).thenReturn("Mock Interest");
        command = new ChangeInterestCommand(mockProduct, mockStrategy);
    }

    @After
    public void tearDown() {
        command = null;
        mockStrategy = null;
        mockProduct = null;
    }

    @Test
    public void test_execute() {
        boolean success = command.execute();
        assertTrue(success);
        verify(mockProduct).setInterest(mockStrategy);
        verify(mockProduct).log(command);
    }

    @Test
    public void test_getValue() {
        double value = command.getValue();
        assertEquals(0.0, value, 0.0);
    }

}
