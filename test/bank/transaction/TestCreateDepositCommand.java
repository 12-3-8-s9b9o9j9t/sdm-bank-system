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
import bank.product.Deposit;
import bank.product.account.AAccount;

public class TestCreateDepositCommand {

    private Bank mockBank = null;
    private Customer mockCustomer = null;
    private AAccount mockAccount = null;
    private Period period = null;
    private Deposit mockDeposit = null;
    private CreateDepositCommand command = null;

    @Before
    public void setUp() throws Exception {
        mockBank = mock(Bank.class);
        mockCustomer = mock(Customer.class);
        mockAccount = mock(AAccount.class);
        when(mockAccount.getId()).thenReturn("ID");
        period = Period.ofMonths(12);
        mockDeposit = mock(Deposit.class);
        command = new CreateDepositCommand(mockBank, mockCustomer, mockAccount, period, 1000.0);
    }

    @After
    public void tearDown() throws Exception {
        mockBank = null;
        mockCustomer = null;
        mockAccount = null;
        period = null;
        mockDeposit = null;
        command = null;
    }

    @Test
    public void test_execute_success() throws Exception{
        when(mockBank.createDeposit(any(Customer.class), any(AAccount.class), any(Period.class), anyDouble())).thenReturn(mockDeposit);
        boolean success = command.execute();
        assertTrue(success);
        verify(mockBank).createDeposit(mockCustomer, mockAccount, period, 1000.0);
        verify(mockDeposit).log(command);
    }

    @Test
    public void test_execute_failure() throws Exception {
        when(mockBank.createDeposit(any(Customer.class), any(AAccount.class), any(Period.class), anyDouble())).thenThrow(new InvalidInputException("test"));
        boolean success = command.execute();
        assertFalse(success);
        verify(mockBank).createDeposit(mockCustomer, mockAccount, period, 1000.0);
        verify(mockDeposit, never()).log(command);
    }

    @Test
    public void test_getValue() {
        double value = command.getValue();
        assertEquals(0, value, 0.0);
    }

}
