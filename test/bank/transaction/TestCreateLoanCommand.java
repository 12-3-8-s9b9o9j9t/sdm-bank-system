package bank.transaction;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Period;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidInputException;
import bank.product.Loan;
import bank.product.account.AAccount;

public class TestCreateLoanCommand {
    
    Bank mockBank = null;
    Customer mockCustomer = null;
    AAccount mockAccount = null;
    Period period = null;
    Loan mockLoan = null;
    CreateLoanCommand command = null;

    @Before
    public void setUp() throws Exception {
        mockBank = mock(Bank.class);
        mockCustomer = mock(Customer.class);
        mockAccount = mock(AAccount.class);
        when(mockAccount.getId()).thenReturn("ID");
        period = Period.ofMonths(12);
        mockLoan = mock(Loan.class);
        command = new CreateLoanCommand(mockBank, mockCustomer, mockAccount, period, 1000.0);
    }

    @After
    public void tearDown() throws Exception {
        mockBank = null;
        mockCustomer = null;
        mockAccount = null;
        period = null;
        mockLoan = null;
        command = null;
    }

    @Test
    public void test_execute_success() throws Exception {
        when(mockBank.createLoan(any(Customer.class), any(AAccount.class), any(Period.class), anyDouble())).thenReturn(mockLoan);
        boolean success = command.execute();
        assertTrue(success);
        verify(mockBank).createLoan(mockCustomer, mockAccount, period, 1000.0);
        verify(mockLoan).log(command);
    }

    @Test
    public void test_execute_failure() throws Exception {
        when(mockBank.createLoan(any(Customer.class), any(AAccount.class), any(Period.class), anyDouble())).thenThrow(new InvalidInputException("test"));
        boolean success = command.execute();
        assertFalse(success);
        verify(mockBank).createLoan(mockCustomer, mockAccount, period, 1000.0);
        verify(mockLoan, never()).log(command);
    }

    @Test
    public void test_getValue() {
        double value = command.getValue();
        assertEquals(-1000.0, value, 0.0);
    }

}
