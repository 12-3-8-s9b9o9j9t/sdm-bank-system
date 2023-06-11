package bank.product;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

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
import bank.transaction.ChargeProductCommand;
import bank.transaction.SupplyProductCommand;

public class TestLoan {

    private AAccount mockAccount = null;
    private Bank mockBank = null;
    private LocalDate fixedTime = null;
    private MockedStatic<LocalDate> mockStatic = null;
    private IVisitor mockVisitor = null;
    private Loan loan = null;
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
        when(mockVisitor.visitLoan(any(Loan.class))).thenReturn("Loan");
        supplyProductCommand = mockConstruction(SupplyProductCommand.class);
        chargeProductCommand = mockConstruction(ChargeProductCommand.class, 
            (mock, context) -> {
                when(mock.execute()).thenReturn(true);
            });
        fixedInterestStrategy = mockConstruction(FixedInterestStrategy.class, 
            (mock, context) -> {
                when(mock.calculate(any(Loan.class))).thenReturn(100.0);
            });
        loan = new Loan("ID", mockBank, mockAccount, Period.ofMonths(6), 1000.0);
    }

    @After
    public void tearDown() {
        mockAccount = null;
        mockBank = null;
        fixedTime = null;
        loan = null;
        mockStatic.close();
        supplyProductCommand.close();
        chargeProductCommand.close();
        fixedInterestStrategy.close();
    }

    @Test
    public void test_initialization() {
        assertEquals(1, supplyProductCommand.constructed().size());
        verify(supplyProductCommand.constructed().get(0)).execute();
        assertEquals(1, fixedInterestStrategy.constructed().size());
        assertEquals(fixedInterestStrategy.constructed().get(0), loan.getInterest());
        assertEquals(fixedTime.plusMonths(6), loan.getTargetDate());
    }

    @Test
    public void test_supply_success() throws Exception {
        loan.supply(100.0);
        assertEquals(1, chargeProductCommand.constructed().size());
        verify(chargeProductCommand.constructed().get(0)).execute();
        assertEquals(900.0, loan.getBalance(), 0.001);
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_supply_failure_amount() throws Exception {
        try {
            loan.supply(-100.0);
        } finally {
            assertEquals(0, chargeProductCommand.constructed().size());
            assertEquals(1000.0, loan.getBalance(), 0.0);
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
            loan.supply(100.0);
        } finally {
            assertEquals(1, chargeProductCommand.constructed().size());
            verify(chargeProductCommand.constructed().get(0)).execute();
            assertEquals(1000.0, loan.getBalance(), 0.0);
        }
    }

    @Test
    public void test_calculateInterest() {
        assertEquals(-100.0, loan.calculateInterest(), 0.0);
        assertEquals(1100.0, loan.getBalance(), 0.001);
        verify(fixedInterestStrategy.constructed().get(0)).calculate(loan);
    }

    @Test
    public void test_accept() {
        assertEquals("Loan", loan.accept(mockVisitor));
        verify(mockVisitor).visitLoan(loan);
    }

}
