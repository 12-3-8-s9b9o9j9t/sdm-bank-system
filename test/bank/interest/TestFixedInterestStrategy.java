package bank.interest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import bank.product.AProduct;

public class TestFixedInterestStrategy {
    
    private LocalDate fixedTime = null;
    private MockedStatic<LocalDate> mockStatic = null;
    private AProduct mockProduct = null;
    private FixedInterestStrategy fixedInterestStrategy = null;

    @Before
    public void setUp() throws Exception {
        fixedTime = LocalDate.of(2023, 6, 11);
        mockProduct = mock(AProduct.class);
        when(mockProduct.getBalance()).thenReturn(1000.0);
        when(mockProduct.getCreationDate()).thenReturn(fixedTime.minusYears(3));
        mockStatic = mockStatic(LocalDate.class, CALLS_REAL_METHODS);
        when(LocalDate.now()).thenReturn(fixedTime);
        fixedInterestStrategy = new FixedInterestStrategy(0.05);
    }

    @After
    public void tearDown() {
        fixedTime = null;
        mockStatic.close();
        mockProduct = null;
        fixedInterestStrategy = null;
    }

    @Test
    public void test_calculate() {
        double expected = 1000.0 * 0.05 * 3;
        double actual = fixedInterestStrategy.calculate(mockProduct);
        assertEquals(expected, actual, 0.001);
    }
    
}
