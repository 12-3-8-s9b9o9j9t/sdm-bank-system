package bank.transaction;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.time.LocalDate;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.exception.InvalidTransactionException;
import bank.product.Deposit;
import bank.product.account.AAccount;

public class TestCloseDepositCommand {
    
    private Deposit mockDeposit = null;
    private AAccount mockAccount = null;
    private CloseDepositCommand command = null;
    private LocalDate targetDate = LocalDate.of(2023, 6, 11);

    @Before
    public void setUp() {
        mockDeposit = mock(Deposit.class);
        mockAccount = mock(AAccount.class);
        when(mockDeposit.getAccount()).thenReturn(mockAccount);
        when(mockDeposit.getTargetDate()).thenReturn(targetDate);
        when(mockAccount.getId()).thenReturn("ID");
        command = new CloseDepositCommand(mockDeposit);
    }

    @After
    public void tearDown() {
        command = null;
        mockAccount = null;
        mockDeposit = null;
    }

    @Test
    public void test_execute_success() throws Exception {
        boolean success = command.execute();
        assertTrue(success);
        verify(mockDeposit).close();
        assertFalse(command.getDescription().endsWith(": Failed"));
        verify(mockDeposit).log(command);
    }

    @Test
    public void test_execute_failure() throws Exception {
        doAnswer(invocation -> {
            throw new InvalidTransactionException("test", "test");
        }).when(mockDeposit).close();
        boolean success = command.execute();
        assertFalse(success);
        verify(mockDeposit).close();
        assertTrue(command.getDescription().endsWith(": Failed"));
        verify(mockDeposit).log(command);
    }

    @Test
    public void test_getValue() {
        double value = command.getValue();
        assertEquals(0.0, value, 0.0);
    }

}
