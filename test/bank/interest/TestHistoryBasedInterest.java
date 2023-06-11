package bank.interest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import bank.product.AProduct;
import bank.transaction.ATransactionCommand;

public class TestHistoryBasedInterest {
    
    private LocalDate fixedTime = null;
    private MockedStatic<LocalDate> mockStatic = null;
    private ATransactionCommand mockTransactionPos = null;
    private ATransactionCommand mockTransactionNeg = null;
    private AProduct mockProduct = null;
    private HistoryBasedStrategy historyBasedStrategy = null;
    private HistoryBasedStrategy historyBasedStrategy2 = null;

    @Before
    public void setUp() throws Exception {
        fixedTime = LocalDate.of(2023, 6, 11);
        mockProduct = mock(AProduct.class);
        when(mockProduct.getBalance()).thenReturn(1000.0);
        when(mockProduct.getCreationDate()).thenReturn(fixedTime.minusYears(3));
        mockStatic = mockStatic(LocalDate.class, CALLS_REAL_METHODS);
        when(LocalDate.now()).thenReturn(fixedTime);
        mockTransactionPos = mock(ATransactionCommand.class);
        when(mockTransactionPos.getValue()).thenReturn(100.0);
        mockTransactionNeg = mock(ATransactionCommand.class);
        when(mockTransactionNeg.getValue()).thenReturn(-100.0);
        historyBasedStrategy = new HistoryBasedStrategy(0.05, true);
        historyBasedStrategy2 = new HistoryBasedStrategy(0.05, false);
    }

    @After
    public void tearDown() {
        fixedTime = null;
        mockStatic.close();
        mockTransactionPos = null;
        mockTransactionNeg = null;
        mockProduct = null;
        historyBasedStrategy = null;
    }

    @Test
    public void test_calculate_historyPos() {
        when(mockProduct.getHistory(any(LocalDate.class))).thenReturn(List.of(mockTransactionPos));
        double expected = 1000.0 * 0.05 * 1.2 * 3;
        double actual = historyBasedStrategy.calculate(mockProduct);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void test_calculate_historyNeg() {
        when(mockProduct.getHistory(any(LocalDate.class))).thenReturn(List.of(mockTransactionNeg));
        double expected = 1000.0 * 0.05 * 0.8 * 3;
        double actual = historyBasedStrategy.calculate(mockProduct);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void test_calculate_historyPos2() {
        when(mockProduct.getHistory(any(LocalDate.class))).thenReturn(List.of(mockTransactionPos));
        double expected = 1000.0 * 0.05 * 0.8 * 3;
        double actual = historyBasedStrategy2.calculate(mockProduct);
        assertEquals(expected, actual, 0.001);
    }

    @Test
    public void test_calculate_historyNeg2() {
        when(mockProduct.getHistory(any(LocalDate.class))).thenReturn(List.of(mockTransactionNeg));
        double expected = 1000.0 * 0.05 * 1.2 * 3;
        double actual = historyBasedStrategy2.calculate(mockProduct);
        assertEquals(expected, actual, 0.001);
    }
}
