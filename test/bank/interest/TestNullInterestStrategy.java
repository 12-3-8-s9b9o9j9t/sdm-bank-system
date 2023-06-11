package bank.interest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.product.account.AAccount;

public class TestNullInterestStrategy {
    
    private AAccount mockAccount = null;
    private NullInterestStrategy strategy = null;

    @Before
    public void setUp() {
        mockAccount = mock(AAccount.class);
        strategy = new NullInterestStrategy();
    }

    @After
    public void tearDown() {
        mockAccount = null;
        strategy = null;
    }

    @Test
    public void test_calculate() {
        double interest = strategy.calculate(mockAccount);
        assertEquals(0.0, interest, 0.0);
    }
}
