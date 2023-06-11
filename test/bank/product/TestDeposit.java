package bank.product;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.Period;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import bank.Bank;
import bank.exception.InvalidTransactionException;
import bank.interest.FixedInterestStrategy;
import bank.product.account.AAccount;
import bank.reporter.IVisitor;
import bank.transaction.CalculateInterestCommand;
import bank.transaction.ChargeProductCommand;
import bank.transaction.SupplyProductCommand;

public class TestDeposit {

    private AAccount mockAccount = null;
    private Bank mockBank = null;
    private LocalDate fixedTime = null;
    private MockedStatic<LocalDate> mockStatic = null;
    private IVisitor mockVisitor = null;
    private Deposit deposit = null;
    private MockedConstruction<CalculateInterestCommand> calculateInterestCommand = null;
    private MockedConstruction<SupplyProductCommand> supplyProductCommand = null;
    private MockedConstruction<ChargeProductCommand> chargeProductCommand = null;
    private MockedConstruction<FixedInterestStrategy> fixedInterestStrategy = null;

    @Before
    public void setUp() {
        mockAccount = mock(AAccount.class);
        mockBank = mock(Bank.class);
        fixedTime = LocalDate.of(2023, 6, 11);
        mockStatic = mockStatic(LocalDate.class, CALLS_REAL_METHODS);
        when(LocalDate.now()).thenReturn(fixedTime);
        mockVisitor = mock(IVisitor.class);
        when(mockVisitor.visitDeposit(any(Deposit.class))).thenReturn("Deposit");
        calculateInterestCommand = mockConstruction(CalculateInterestCommand.class);
        supplyProductCommand = mockConstruction(SupplyProductCommand.class,
            (mock, context) -> {
                when(mock.execute()).thenReturn(true);
            });
        chargeProductCommand = mockConstruction(ChargeProductCommand.class, 
            (mock, context) -> {
                when(mock.execute()).thenReturn(true);
            });
        fixedInterestStrategy = mockConstruction(FixedInterestStrategy.class, 
            (mock, context) -> {
                when(mock.calculate(any(Deposit.class))).thenReturn(100.0);
            });
        deposit = new Deposit("ID", mockBank, mockAccount, Period.ofMonths(6), 1000.0);
    }

    @After
    public void tearDown() {
        mockAccount = null;
        mockBank = null;
        fixedTime = null;
        deposit = null;
        mockStatic.close();
        supplyProductCommand.close();
        chargeProductCommand.close();
        fixedInterestStrategy.close();
        calculateInterestCommand.close();
    }

    @Test
    public void test_initialization() {
        assertEquals(1, fixedInterestStrategy.constructed().size());
        assertEquals(fixedInterestStrategy.constructed().get(0), deposit.getInterest());
        assertEquals(fixedTime.plusMonths(6), deposit.getTargetDate());
    }

    @Test
    public void test_supply_success() throws Exception {
        deposit.supply(100.0);
        assertEquals(1, chargeProductCommand.constructed().size());
        verify(chargeProductCommand.constructed().get(0)).execute();
        assertEquals(100.0, deposit.getBalance(), 0.0);
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_supply_failure_amount() throws Exception {
        try {
            deposit.supply(-100.0);
        } finally {
            assertEquals(0, chargeProductCommand.constructed().size());
            assertEquals(0.0, deposit.getBalance(), 0.0);
        }
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_supply_failure_limit() throws Exception {
        try {
            deposit.supply(1001.0);
        } finally {
            assertEquals(0, chargeProductCommand.constructed().size());
            assertEquals(0.0, deposit.getBalance(), 0.0);
        }
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_supply_failure_execute() throws Exception {
        chargeProductCommand.close();
        chargeProductCommand = mockConstruction(ChargeProductCommand.class, 
            (mock, context) -> {
                when(mock.execute()).thenReturn(false);
            });
        try {
            deposit.supply(100.0);
        } finally {
            assertEquals(1, chargeProductCommand.constructed().size());
            verify(chargeProductCommand.constructed().get(0)).execute();
            assertEquals(0.0, deposit.getBalance(), 0.0);
        }
    }

    @Test
    public void test_close_success() throws Exception {
        Field f = deposit.getClass().getDeclaredField("amount");
        f.setAccessible(true);
        f.set(deposit, 100.0);
        assertEquals(100.0, deposit.getBalance(), 0.0);
        deposit.close();
        assertEquals(1, calculateInterestCommand.constructed().size());
        verify(calculateInterestCommand.constructed().get(0)).execute();
        assertEquals(1, supplyProductCommand.constructed().size());
        verify(supplyProductCommand.constructed().get(0)).execute();
        assertEquals(0.0, deposit.getBalance(), 0.0);
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_close_failure() throws Exception {
        Field f = deposit.getClass().getDeclaredField("amount");
        f.setAccessible(true);
        f.set(deposit, 100.0);
        assertEquals(100.0, deposit.getBalance(), 0.0);
        supplyProductCommand.close();
        supplyProductCommand = mockConstruction(SupplyProductCommand.class, 
            (mock, context) -> {
                when(mock.execute()).thenReturn(false);
            });
        try {
            deposit.close();
        } finally {
            assertEquals(1, calculateInterestCommand.constructed().size());
            verify(calculateInterestCommand.constructed().get(0)).execute();
            assertEquals(1, supplyProductCommand.constructed().size());
            verify(supplyProductCommand.constructed().get(0)).execute();
            assertEquals(100.0, deposit.getBalance(), 0.0);
        }
    }

    @Test
    public void test_calculateInterest_targetDateReached() throws Exception {
        Field f = deposit.getClass().getDeclaredField("targetDate");
        f.setAccessible(true);
        LocalDate targetDate = (LocalDate) f.get(deposit);
        f.set(deposit, targetDate.minusMonths(6).minusDays(1)); // simulate 6 month passed
        assertEquals(100.0, deposit.calculateInterest(), 0.0);
        assertEquals(100.0, deposit.getBalance(), 0.0);
        verify(fixedInterestStrategy.constructed().get(0)).calculate(deposit);
    }

    @Test
    public void test_calculateInterest_targetDateNotReached() throws Exception {
        Field f = deposit.getClass().getDeclaredField("targetDate");
        f.setAccessible(true);
        LocalDate targetDate = (LocalDate) f.get(deposit);
        f.set(deposit, targetDate.minusMonths(3)); // simulate 3 month passed
        assertEquals(0.0, deposit.calculateInterest(), 0.0);
        assertEquals(0.0, deposit.getBalance(), 0.0);
        verify(fixedInterestStrategy.constructed().get(0), never()).calculate(deposit);
    }

    @Test
    public void test_accept() {
        assertEquals("Deposit", deposit.accept(mockVisitor));
        verify(mockVisitor).visitDeposit(deposit);
    }

}
