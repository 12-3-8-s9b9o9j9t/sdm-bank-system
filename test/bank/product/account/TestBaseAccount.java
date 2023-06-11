package bank.product.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidTransactionException;
import bank.interest.FixedInterestStrategy;
import bank.reporter.IVisitor;

public class TestBaseAccount {

    private Customer mockCustomer = null;
    private Bank mockBank = null;
    private IVisitor mockVisitor = null;
    private BaseAccount account = null;
    private MockedConstruction<FixedInterestStrategy> fixedInterestStrategy = null;

    @Before
    public void setUp() {
        mockCustomer = mock(Customer.class);
        mockBank = mock(Bank.class);
        mockVisitor = mock(IVisitor.class);
        when(mockVisitor.visitBaseAccount(any(BaseAccount.class))).thenReturn("Account");
        fixedInterestStrategy = mockConstruction(FixedInterestStrategy.class, 
            (mock, context) -> {
                when(mock.calculate(any(BaseAccount.class))).thenReturn(100.0);
            });
        account = new BaseAccount("ID", mockBank, mockCustomer);
    }

    @After
    public void tearDown() {
        mockCustomer = null;
        mockBank = null;
        mockVisitor = null;
        account = null;
        fixedInterestStrategy.close();
    }

    @Test
    public void test_initialization() {
        assertEquals(1, fixedInterestStrategy.constructed().size());
        assertEquals(fixedInterestStrategy.constructed().get(0), account.getInterest());
    }

    @Test
    public void test_charge_success() throws Exception {
        Field f = account.getClass().getDeclaredField("balance");
        f.setAccessible(true);
        f.set(account, 200.0);
        account.charge(100.0);
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_charge_failure_amount() throws Exception {
        Field f = account.getClass().getDeclaredField("balance");
        f.setAccessible(true);
        f.set(account, 200.0);
        try {
            account.charge(-100.0);
        } finally {
            assertEquals(200.0, account.getBalance(), 0.0);
        }
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_charge_failure_amount2() throws Exception {
        Field f = account.getClass().getDeclaredField("balance");
        f.setAccessible(true);
        f.set(account, 200.0);
        try {
            account.charge(300.0);
        } finally {
            assertEquals(200.0, account.getBalance(), 0.0);
        }
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_charge_failure_limit() throws Exception {
        try {
            account.charge(2000.0);
        } finally {
            assertEquals(0.0, account.getBalance(), 0.0);
        }
    }

    @Test
    public void test_supply_success() throws Exception {
        account.supply(100.0);
        assertEquals(100.0, account.getBalance(), 0.0);
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_supply_failure_amount() throws Exception {
        try {
            account.supply(-100.0);
        } finally {
            assertEquals(0.0, account.getBalance(), 0.0);
        }
    }

    @Test
    public void test_calculateInterest() {
        assertEquals(100.0, account.calculateInterest(), 0.0);
        assertEquals(100.0, account.getBalance(), 0.0);
        verify(fixedInterestStrategy.constructed().get(0)).calculate(account);
    }

    @Test
    public void test_accept() {
        assertEquals("Account", account.accept(mockVisitor));
        verify(mockVisitor).visitBaseAccount(account);
    }
    
}
