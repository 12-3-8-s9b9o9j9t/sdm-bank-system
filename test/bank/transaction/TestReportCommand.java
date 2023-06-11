package bank.transaction;

import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.Customer;
import bank.product.AProduct;
import bank.reporter.IVisitor;

public class TestReportCommand {
    
    private ATransactionCommand mockATransactionCommand = null;
    private AProduct mockProduct = null;
    private AProduct mockProduct2 = null;
    private Customer mockCustomer = null;
    private IVisitor mockIVisitor = null;
    private ReportCommand command_noFilter = null;
    private ReportCommand command_filter = null;

    @Before
    public void setUp() {
        mockATransactionCommand = mock(ATransactionCommand.class);
        mockProduct = mock(AProduct.class);
        mockProduct2 = mock(AProduct.class);
        mockCustomer = mock(Customer.class);
        mockIVisitor = mock(IVisitor.class);
        when(mockATransactionCommand.accept(any(IVisitor.class))).thenReturn("T");
        when(mockCustomer.accept(any(IVisitor.class))).thenReturn("C");
        when(mockProduct.accept(any(IVisitor.class))).thenReturn("P");
        when(mockProduct.getHistory(any(LocalDate.class))).thenReturn(List.of(mockATransactionCommand));
        when(mockProduct2.accept(any(IVisitor.class))).thenReturn("P2");
        when(mockProduct2.getHistory(any(LocalDate.class))).thenReturn(List.of());
        when(mockCustomer.getProducts()).thenReturn(Map.of("ID", mockProduct, "ID2", mockProduct2));
        command_noFilter = new ReportCommand(new StringBuilder(), null, mockCustomer);
        command_filter = new ReportCommand(new StringBuilder(), p -> p == mockProduct, mockCustomer);
    }

    @After
    public void tearDown() {
        mockATransactionCommand = null;
        mockProduct = null;
        mockProduct2 = null;
        mockCustomer = null;
        mockIVisitor = null;
        command_noFilter = null;
        command_filter = null;
    }

    @Test
    public void test_execute_noFilter() throws Exception {
        Field f = command_noFilter.getClass().getDeclaredField("visitor");
        f.setAccessible(true);
        f.set(command_noFilter, mockIVisitor);
        f = command_noFilter.getClass().getDeclaredField("builder");
        f.setAccessible(true);
        StringBuilder builder = (StringBuilder) f.get(command_noFilter);
        assertTrue(builder.length() == 0);
        boolean result = command_noFilter.execute();
        assertTrue(result);
        verify(mockCustomer).accept(mockIVisitor);
        verify(mockProduct).accept(mockIVisitor);
        verify(mockATransactionCommand).accept(mockIVisitor);
        verify(mockProduct2).accept(mockIVisitor);
        assertTrue(builder.length() > 0);
    }

    @Test
    public void test_execute_filter() throws Exception {
        Field f = command_filter.getClass().getDeclaredField("visitor");
        f.setAccessible(true);
        f.set(command_filter, mockIVisitor);
        f = command_filter.getClass().getDeclaredField("builder");
        f.setAccessible(true);
        StringBuilder builder = (StringBuilder) f.get(command_filter);
        assertTrue(builder.length() == 0);
        boolean result = command_filter.execute();
        assertTrue(result);
        verify(mockCustomer).accept(mockIVisitor);
        verify(mockProduct).accept(mockIVisitor);
        verify(mockATransactionCommand).accept(mockIVisitor);
        verify(mockProduct2, never()).accept(mockIVisitor);
        assertTrue(builder.length() > 0);
    }

    @Test
    public void test_getValue() {
        double result = command_noFilter.getValue();
        assertEquals(0.0, result, 0.0);
    }

}
