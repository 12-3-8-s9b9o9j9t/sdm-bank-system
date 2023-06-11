package bank.product.account;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

import bank.exception.InvalidTransactionException;
import bank.reporter.IVisitor;

public class TestDebitDecorator {

    private AAccount mockWrapee = null;
    private IVisitor mockVisitor = null;
    private DebitDecorator account = null;

    @Before
    public void setUp() {
        mockWrapee = mock(AAccount.class);
        mockVisitor = mock(IVisitor.class);
        when(mockVisitor.visitDebitAccount(any(DebitDecorator.class))).thenReturn("Debit");
        account = new DebitDecorator(mockWrapee, 100.0);
    }

    @Test
    public void test_charge_success_noOverdraft() throws Exception {
        when(mockWrapee.getBalance()).thenReturn(200.0);
        account.charge(100.0);
        verify(mockWrapee).charge(100.0);
        assertEquals(0.0, account.getOverdraft(), 0.0);
    }

    @Test
    public void test_charge_success_withOverdraft() throws Exception {
        when(mockWrapee.getBalance()).thenReturn(50.0);
        account.charge(100.0);
        verify(mockWrapee).charge(50.0);
        assertEquals(50.0, account.getOverdraft(), 0.0);
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_charge_failure_amount() throws Exception {
        try {
            account.charge(-100.0);
        } finally {
            verify(mockWrapee, never()).charge(anyDouble());
            assertEquals(0.0, account.getOverdraft(), 0.0);
        }
    }

    @Test(expected = InvalidTransactionException.class)
    public void test_charge_failure_overdraft() throws Exception {
        when(mockWrapee.getBalance()).thenReturn(50.0);
        try {
            account.charge(200.0);
        } finally {
            verify(mockWrapee, never()).charge(anyDouble());
            assertEquals(0.0, account.getOverdraft(), 0.0);
        }
    }

    @Test
    public void test_supply_success_noOverdraft() throws Exception {
        account.supply(100.0);
        verify(mockWrapee).supply(100.0);
        assertEquals(0.0, account.getOverdraft(), 0.0);
    }

    @Test
    public void test_supply_success_withOverdraft() throws Exception {
        Field f = account.getClass().getDeclaredField("overdraft");
        f.setAccessible(true);
        f.set(account, 50.0);
        account.supply(100.0);
        verify(mockWrapee).supply(50.0);
        assertEquals(0.0, account.getOverdraft(), 0.0);
    }

    @Test
    public void test_calculateInterest() {
        account.calculateInterest();
        verify(mockWrapee).calculateInterest();
    }

    @Test
    public void test_accept() {
        assertEquals("Debit", account.accept(mockVisitor));
        verify(mockVisitor).visitDebitAccount(account);
    }
}
