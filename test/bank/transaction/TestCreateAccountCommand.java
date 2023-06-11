package bank.transaction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidCustomerException;
import bank.product.account.AAccount;

public class TestCreateAccountCommand {

    private Bank mockBank = null;
    private Customer mockCustomer = null;
    private AAccount mockAccount = null;
    private CreateAccountCommand command = null;

    @Before
    public void setUp() throws Exception {
        mockBank = mock(Bank.class);
        mockCustomer = mock(Customer.class);
        when(mockCustomer.getId()).thenReturn(1234);
        when(mockCustomer.getName()).thenReturn("John Doe");
        mockAccount = mock(AAccount.class);
        command = new CreateAccountCommand(mockBank, mockCustomer);
    }

    @After
    public void tearDown() throws Exception {
        mockBank = null;
        mockCustomer = null;
        mockAccount = null;
        command = null;
    }

    @Test
    public void test_execute_success() throws Exception{
        when(mockBank.createAccount(any(Customer.class))).thenReturn(mockAccount);
        boolean success = command.execute();
        assertTrue(success);
        verify(mockBank).createAccount(mockCustomer);
        verify(mockAccount).log(command);
    }

    @Test
    public void test_execute_failure() throws Exception {
        when(mockBank.createAccount(any(Customer.class))).thenThrow(new InvalidCustomerException("test"));
        boolean success = command.execute();
        assertFalse(success);
        verify(mockBank).createAccount(mockCustomer);
        verify(mockAccount, never()).log(command);
    }

    @Test
    public void test_getValue() {
        double value = command.getValue();
        assertEquals(0, value, 0.0);
    }

}
