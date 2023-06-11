package bank.reporter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import bank.Customer;
import bank.product.Credit;
import bank.product.Deposit;
import bank.product.Loan;
import bank.product.account.AAccount;
import bank.product.account.BaseAccount;
import bank.product.account.DebitDecorator;
import bank.transaction.ATransactionCommand;

public class TestReporterVisitor {
    
    private Customer mockCustomer = null;
    private AAccount mockAccount = null;
    private Loan mockLoan = null;
    private Deposit mockDeposit = null;
    private Credit mockCredit = null;
    private BaseAccount mockBaseAccount = null;
    private DebitDecorator mockDebitDecorator = null;
    private ATransactionCommand mockTransactionCommand = null;
    private ReporterVisitor visitor = null;

    @Before
    public void setUp() {
        mockCustomer = mock(Customer.class);
        mockAccount = mock(AAccount.class);
        mockLoan = mock(Loan.class);
        mockDeposit = mock(Deposit.class);
        when(mockLoan.getAccount()).thenReturn(mockAccount);
        when(mockDeposit.getAccount()).thenReturn(mockAccount);
        mockCredit = mock(Credit.class);
        mockBaseAccount = mock(BaseAccount.class);
        mockDebitDecorator = mock(DebitDecorator.class);
        mockTransactionCommand = mock(ATransactionCommand.class);
        visitor = new ReporterVisitor();
    }

    @After
    public void tearDown() {
        mockCustomer = null;
        mockAccount = null;
        mockLoan = null;
        mockDeposit = null;
        mockCredit = null;
        mockBaseAccount = null;
        mockDebitDecorator = null;
        mockTransactionCommand = null;
        visitor = null;
    }

    @Test
    public void test_visitCustomer() {
        String result = visitor.visitCustomer(mockCustomer);
        assertFalse(result.isEmpty());
    }

    @Test
    public void test_visitLoan() {
        String result = visitor.visitLoan(mockLoan);
        assertFalse(result.isEmpty());
    }

    @Test
    public void test_visitDeposit() {
        String result = visitor.visitDeposit(mockDeposit);
        assertFalse(result.isEmpty());
    }

    @Test
    public void test_visitCredit() {
        String result = visitor.visitCredit(mockCredit);
        assertFalse(result.isEmpty());
    }

    @Test
    public void test_visitBaseAccount() {
        String result = visitor.visitBaseAccount(mockBaseAccount);
        assertFalse(result.isEmpty());
    }

    @Test
    public void test_visitDebitAccount() {
        String result = visitor.visitDebitAccount(mockDebitDecorator);
        assertFalse(result.isEmpty());
    }

    @Test
    public void test_visitTransaction() {
        String result = visitor.visitTransaction(mockTransactionCommand);
        assertFalse(result.isEmpty());
    }
}
