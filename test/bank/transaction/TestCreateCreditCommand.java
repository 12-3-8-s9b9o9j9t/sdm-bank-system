package bank.transaction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidInputException;
import bank.product.Credit;

public class TestCreateCreditCommand {

    private Bank mockBank = null;
    private Customer mockCustomer = null;
    private Credit mockCredit = null;
    private CreateCreditCommand command = null;

    @Before
    public void setUp() throws Exception {
        mockBank = mock(Bank.class);
        mockCustomer = mock(Customer.class);
        when(mockCustomer.getId()).thenReturn(1234);
        when(mockCustomer.getName()).thenReturn("John Doe");
        mockCredit = mock(Credit.class);
        command = new CreateCreditCommand(mockBank, mockCustomer, 1000.0);
    }

    @After
    public void tearDown() throws Exception {
        mockBank = null;
        mockCustomer = null;
        mockCredit = null;
        command = null;
    }

    @Test
    public void test_execute_success() throws Exception{
        when(mockBank.createCredit(any(Customer.class), anyDouble())).thenReturn(mockCredit);
        boolean success = command.execute();
        assertTrue(success);
        verify(mockBank).createCredit(mockCustomer, 1000.0);
        verify(mockCredit).log(command);
    }

    @Test
    public void test_execute_failure() throws Exception {
        when(mockBank.createCredit(any(Customer.class), anyDouble())).thenThrow(new InvalidInputException("test"));
        boolean success = command.execute();
        assertFalse(success);
        verify(mockBank).createCredit(mockCustomer, 1000.0);
        verify(mockCredit, never()).log(command);
    }

    @Test
    public void test_getValue() {
        double value = command.getValue();
        assertEquals(0, value, 0.0);
    }

}
