package bank.transaction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidInputException;
import bank.product.account.AAccount;

public class TestExtendAccountWithDebitCommand {
    
    private Bank mockBank = null;
    private Customer mockCustomer = null;
    private AAccount mockAccount = null;
    private AAccount mockDebitAccount = null;
    private ExtendAccountWithDebitCommand command = null;

    @Before
    public void setUp() {
        mockBank = mock(Bank.class);
        mockCustomer = mock(Customer.class);
        mockAccount = mock(AAccount.class);
        mockDebitAccount = mock(AAccount.class);
        when(mockAccount.getId()).thenReturn("ID");
        command = new ExtendAccountWithDebitCommand(mockBank, mockCustomer, mockAccount, 1000.0);
    }

    @After
    public void tearDown() {
        mockBank = null;
        mockCustomer = null;
        mockAccount = null;
        command = null;
    }

    @Test
    public void test_execute_success() throws Exception {
        when(mockBank.extendAccountWithDebit(any(Customer.class), any(AAccount.class), anyDouble())).thenReturn(mockDebitAccount);
        boolean success = command.execute();
        assertTrue(success);
        verify(mockBank).extendAccountWithDebit(mockCustomer, mockAccount, 1000.0);
        verify(mockDebitAccount).log(command);
    }

    @Test
    public void test_execute_failure() throws Exception {
        when(mockBank.extendAccountWithDebit(any(Customer.class), any(AAccount.class), anyDouble())).thenThrow(new InvalidInputException("test"));
        boolean success = command.execute();
        assertFalse(success);
        verify(mockBank).extendAccountWithDebit(mockCustomer, mockAccount, 1000.0);
        verify(mockAccount, never()).log(command);
    }

    @Test
    public void test_getValue() {
        double value = command.getValue();
        assertEquals(0.0, value, 0.0);
    }

}
