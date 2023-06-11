package bank.product;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import bank.Bank;
import bank.exception.InvalidTransactionException;
import bank.interest.HistoryBasedStrategy;
import bank.reporter.IVisitor;

public class TestCredit {

    private Bank mockBank = null;
    private IVisitor mockVisitor = null;
    private Credit credit = null;
    private MockedConstruction<HistoryBasedStrategy> historyBasedStrategy = null;

    @Before
    public void setUp() {
        mockBank = mock(Bank.class);
        mockVisitor = mock(IVisitor.class);
        when(mockVisitor.visitCredit(any(Credit.class))).thenReturn("Credit");
        historyBasedStrategy = mockConstruction(HistoryBasedStrategy.class, 
            (mock, context) -> {
                when(mock.calculate(any(Credit.class))).thenReturn(100.0);
            });
        credit = new Credit("ID", mockBank, 1000.0);
    }

    @After
    public void tearDown() {
        mockBank = null;
        mockVisitor = null;
        credit = null;
        historyBasedStrategy.close();
    }

    @Test
    public void test_initialization() {
        assertEquals(1, historyBasedStrategy.constructed().size());
        assertEquals(historyBasedStrategy.constructed().get(0), credit.getInterest());
    }

    @Test
    public void test_charge_success() throws Exception {
        credit.charge(100.0);
        assertEquals(100.0, credit.getBalance(), 0.0);
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_charge_failure_amount() throws Exception {
        try {
            credit.charge(-100.0);
        } finally {
            assertEquals(0.0, credit.getBalance(), 0.0);
        }
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_charge_failure_limit() throws Exception {
        try {
            credit.charge(2000.0);
        } finally {
            assertEquals(0.0, credit.getBalance(), 0.0);
        }
    }

    @Test
    public void test_supply_success() throws Exception {
        Field f = credit.getClass().getDeclaredField("amount");
        f.setAccessible(true);
        f.set(credit, 100.0);
        credit.supply(100.0);
        assertEquals(0.0, credit.getBalance(), 0.0);
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_supply_failure_amount() throws Exception {
        try {
            credit.supply(-100.0);
        } finally {
            assertEquals(0.0, credit.getBalance(), 0.0);
        }
    }

    @Test
    public void test_calculateInterest() {
        assertEquals(-100.0, credit.calculateInterest(), 0.0);
        assertEquals(100.0, credit.getBalance(), 0.0);
        verify(historyBasedStrategy.constructed().get(0)).calculate(credit);
    }

    @Test
    public void test_accept() {
        assertEquals("Credit", credit.accept(mockVisitor));
        verify(mockVisitor).visitCredit(credit);
    }
    
}
