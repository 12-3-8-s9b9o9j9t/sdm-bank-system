package bank;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.time.Period;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import bank.exception.InvalidAmountException;
import bank.exception.InvalidPeriodException;
import bank.exception.InvalidProductException;
import bank.product.Deposit;
import bank.product.IChargeable;
import bank.product.ISuppliable;
import bank.product.Product;
import bank.product.account.AAccount;
import bank.reporter.IVisitor;
import bank.transaction.ChargeProductCommand;
import bank.transaction.CloseDepositCommand;
import bank.transaction.CreateAccountCommand;
import bank.transaction.CreateCreditCommand;
import bank.transaction.CreateDepositCommand;
import bank.transaction.CreateLoanCommand;
import bank.transaction.ExtendAccountWithDebitCommand;
import bank.transaction.ReportCommand;
import bank.transaction.SupplyProductCommand;
import bank.transaction.transfer.TransferCommand;

public class TestCustomer {
    
    private Bank mockBank = null;
    private Customer customer = null;
    private AAccount mockAccount = null;
    private Deposit mockDeposit = null;
    private Product mockSuppliable = null;
    private Product mockChargeable = null;
    private TransferCommand mockTransferCommand = null;
    private IVisitor mockVisitor = null;
    private MockedConstruction<CreateAccountCommand> createAccountCommand = null;
    private MockedConstruction<CreateCreditCommand> createCreditCommand = null;
    private MockedConstruction<CreateLoanCommand> createLoanCommand = null;
    private MockedConstruction<CreateDepositCommand> createDepositCommand = null;
    private MockedConstruction<ExtendAccountWithDebitCommand> extendAccountWithDebitCommand = null;
    private MockedConstruction<CloseDepositCommand> closeDepositCommand = null;
    private MockedConstruction<SupplyProductCommand> supplyProductCommand = null;
    private MockedConstruction<ChargeProductCommand> chargeProductCommand = null;
    private MockedConstruction<TransferCommand> transferCommand = null;

    private StringBuilder builder = null;
    private MockedConstruction<ReportCommand> reportCommand = null;


    @Before
    public void setUp() {
        mockBank = mock(Bank.class);
        customer = new Customer(1, "John Doe", "1234", mockBank);
        mockAccount = mock(AAccount.class);
        mockDeposit = mock(Deposit.class);
        mockSuppliable = mock(Product.class, withSettings().extraInterfaces(ISuppliable.class));
        mockChargeable = mock(Product.class, withSettings().extraInterfaces(IChargeable.class));
        mockTransferCommand = mock(TransferCommand.class);
        mockVisitor = mock(IVisitor.class);
        createAccountCommand = mockConstruction(CreateAccountCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(true);
        });
        createCreditCommand = mockConstruction(CreateCreditCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(true);
        });
        createLoanCommand = mockConstruction(CreateLoanCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(true);
        });
        createDepositCommand = mockConstruction(CreateDepositCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(true);
        });
        extendAccountWithDebitCommand = mockConstruction(ExtendAccountWithDebitCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(true);
        });
        closeDepositCommand = mockConstruction(CloseDepositCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(true);
        });
        supplyProductCommand = mockConstruction(SupplyProductCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(true);
        });
        chargeProductCommand = mockConstruction(ChargeProductCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(true);
        });
        transferCommand = mockConstruction(TransferCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(true);
            when(mock.getId()).thenReturn("TID");
        });

        reportCommand = mockConstruction(ReportCommand.class, (mock, context) -> {
            builder = (StringBuilder)context.arguments().get(0);
            doAnswer(invocation -> {
                builder.append("report");
                return null;
            }).when(mock).execute();
        });
    }

    @After
    public void tearDown() {
        mockBank = null;
        customer = null;
        mockAccount = null;
        mockDeposit = null;
        mockSuppliable = null;
        mockChargeable = null;
        mockTransferCommand = null;
        mockVisitor = null;
        createAccountCommand.close();
        createCreditCommand.close();
        createLoanCommand.close();
        createDepositCommand.close();
        extendAccountWithDebitCommand.close();
        closeDepositCommand.close();
        supplyProductCommand.close();
        chargeProductCommand.close();
        transferCommand.close();
        builder = null;
        reportCommand.close();
    }

    @Test
    public void test_createAccount() {
        boolean success = customer.createAcount();
        assertEquals(1, createAccountCommand.constructed().size());
        verify(createAccountCommand.constructed().get(0), times(1)).execute();
        assertTrue(success);
    }

    @Test
    public void test_createCredit() throws Exception {
        boolean success = customer.createCredit(1000.0);
        assertEquals(1, createCreditCommand.constructed().size());
        verify(createCreditCommand.constructed().get(0), times(1)).execute();
        assertTrue(success);
    }

    @Test(expected = InvalidAmountException.class)
    public void test_createCredit_invalidAmount() throws Exception {
        try {
            customer.createCredit(-1000.0);
        } finally {
            assertEquals(0, createCreditCommand.constructed().size());
        }
    }

    @Test
    public void test_createLoan() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        boolean success = customer.createLoan(mockAccount, Period.ofMonths(12), 1000.0);
        assertEquals(1, createLoanCommand.constructed().size());
        verify(createLoanCommand.constructed().get(0), times(1)).execute();
        assertTrue(success);
    }

    @Test(expected = InvalidAmountException.class)
    public void test_createLoan_invalidAmount() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        try {
            customer.createLoan(mockAccount, Period.ofMonths(12), -1000.0);
        } finally {
            assertEquals(0, createLoanCommand.constructed().size());
        }
    }

    @Test(expected = InvalidProductException.class)
    public void test_createLoan_invalidProduct() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        try {
            customer.createLoan(mockAccount, Period.ofMonths(12), 1000.0);
        } finally {
            assertEquals(0, createLoanCommand.constructed().size());
        }
    }

    @Test(expected = InvalidPeriodException.class)
    public void test_createLoan_invalidPeriod() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        try {
            customer.createLoan(mockAccount, Period.ofMonths(0), 1000.0);
        } finally {
            assertEquals(0, createLoanCommand.constructed().size());
        }
    }

    @Test
    public void test_createDeposit() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        boolean success = customer.createDeposit(mockAccount, Period.ofMonths(12), 1000.0);
        assertEquals(1, createDepositCommand.constructed().size());
        verify(createDepositCommand.constructed().get(0), times(1)).execute();
        assertTrue(success);
    }

    @Test(expected = InvalidAmountException.class)
    public void test_createDeposit_invalidAmount() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        try {
            customer.createDeposit(mockAccount, Period.ofMonths(12), -1000.0);
        } finally {
            assertEquals(0, createDepositCommand.constructed().size());
        }
    }

    @Test(expected = InvalidProductException.class)
    public void test_createDeposit_invalidProduct() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        try {
            customer.createDeposit(mockAccount, Period.ofMonths(12), 1000.0);
        } finally {
            assertEquals(0, createDepositCommand.constructed().size());
        }
    }

    @Test(expected = InvalidPeriodException.class)
    public void test_createDeposit_invalidPeriod() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        try {
            customer.createDeposit(mockAccount, Period.ofMonths(0), 1000.0);
        } finally {
            assertEquals(0, createDepositCommand.constructed().size());
        }
    }

    @Test
    public void test_extendAccountWithDebit() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        boolean success = customer.extendAccountWithDebit(mockAccount, 1000.0);
        assertEquals(1, extendAccountWithDebitCommand.constructed().size());
        verify(extendAccountWithDebitCommand.constructed().get(0), times(1)).execute();
        assertTrue(success);
    }

    @Test(expected = InvalidAmountException.class)
    public void test_extendAccountWithDebit_invalidAmount() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        try {
            customer.extendAccountWithDebit(mockAccount, -1000.0);
        } finally {
            assertEquals(0, extendAccountWithDebitCommand.constructed().size());
        }
    }

    @Test(expected = InvalidProductException.class)
    public void test_extendAccountWithDebit_invalidProduct() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        try {
            customer.extendAccountWithDebit(mockAccount, 1000.0);
        } finally {
            assertEquals(0, extendAccountWithDebitCommand.constructed().size());
        }
    }

    @Test
    public void test_closeDeposit_success() throws Exception {
        when(mockDeposit.getId()).thenReturn("ID");
        customer.addProduct(mockDeposit);
        boolean success = customer.closeDeposit(mockDeposit);
        assertEquals(1, closeDepositCommand.constructed().size());
        verify(closeDepositCommand.constructed().get(0), times(1)).execute();
        assertEquals(0, customer.getProducts().size());
        assertTrue(success);
    }

    @Test
    public void test_closeDeposit_failure() throws Exception {
        when(mockDeposit.getId()).thenReturn("ID");
        customer.addProduct(mockDeposit);
        closeDepositCommand.close();
        closeDepositCommand = mockConstruction(CloseDepositCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(false);
        });
        boolean success = customer.closeDeposit(mockDeposit);
        assertEquals(1, closeDepositCommand.constructed().size());
        verify(closeDepositCommand.constructed().get(0), times(1)).execute();
        assertEquals(1, customer.getProducts().size());
        assertFalse(success);
    }

    @Test(expected = InvalidProductException.class)
    public void test_closeDeposit_invalidProduct() throws Exception {
        when(mockDeposit.getId()).thenReturn("ID");
        try {
            customer.closeDeposit(mockDeposit);
        } finally {
            assertEquals(0, closeDepositCommand.constructed().size());
        }
    }

    @Test
    public void test_supplyProduct_success() throws Exception {
        when(mockSuppliable.getId()).thenReturn("ID");
        customer.addProduct(mockSuppliable);
        boolean success = customer.supplyProduct((ISuppliable)mockSuppliable, 1000.0);
        assertEquals(1, supplyProductCommand.constructed().size());
        verify(supplyProductCommand.constructed().get(0), times(1)).execute();
        assertTrue(success);
    }

    @Test
    public void test_supplyProduct_failure() throws Exception {
        when(mockSuppliable.getId()).thenReturn("ID");
        customer.addProduct(mockSuppliable);
        supplyProductCommand.close();
        supplyProductCommand = mockConstruction(SupplyProductCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(false);
        });
        boolean success = customer.supplyProduct((ISuppliable)mockSuppliable, 1000.0);
        assertEquals(1, supplyProductCommand.constructed().size());
        verify(supplyProductCommand.constructed().get(0), times(1)).execute();
        assertFalse(success);
    }

    @Test(expected = InvalidAmountException.class)
    public void test_supplyProduct_invalidAmount() throws Exception {
        when(mockSuppliable.getId()).thenReturn("ID");
        customer.addProduct(mockSuppliable);
        try {
            customer.supplyProduct((ISuppliable)mockSuppliable, -1000.0);
        } finally {
            assertEquals(0, supplyProductCommand.constructed().size());
        }
    }

    @Test(expected = InvalidProductException.class)
    public void test_supplyProduct_invalidProduct() throws Exception {
        when(mockSuppliable.getId()).thenReturn("ID");
        try {
            customer.supplyProduct((ISuppliable)mockSuppliable, 1000.0);
        } finally {
            assertEquals(0, supplyProductCommand.constructed().size());
        }
    }

    @Test
    public void test_chargeProduct_success() throws Exception {
        when(mockChargeable.getId()).thenReturn("ID");
        customer.addProduct(mockChargeable);
        boolean success = customer.chargeProduct((IChargeable)mockChargeable, 1000.0);
        assertEquals(1, chargeProductCommand.constructed().size());
        verify(chargeProductCommand.constructed().get(0), times(1)).execute();
        assertTrue(success);
    }

    @Test
    public void test_chargeProduct_failure() throws Exception {
        when(mockChargeable.getId()).thenReturn("ID");
        customer.addProduct(mockChargeable);
        chargeProductCommand.close();
        chargeProductCommand = mockConstruction(ChargeProductCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(false);
        });
        boolean success = customer.chargeProduct((IChargeable)mockChargeable, 1000.0);
        assertEquals(1, chargeProductCommand.constructed().size());
        verify(chargeProductCommand.constructed().get(0), times(1)).execute();
        assertFalse(success);
    }

    @Test(expected = InvalidAmountException.class)
    public void test_chargeProduct_invalidAmount() throws Exception {
        when(mockChargeable.getId()).thenReturn("ID");
        customer.addProduct(mockChargeable);
        try {
            customer.chargeProduct((IChargeable)mockChargeable, -1000.0);
        } finally {
            assertEquals(0, chargeProductCommand.constructed().size());
        }
    }

    @Test(expected = InvalidProductException.class)
    public void test_chargeProduct_invalidProduct() throws Exception {
        when(mockChargeable.getId()).thenReturn("ID");
        try {
            customer.chargeProduct((IChargeable)mockChargeable, 1000.0);
        } finally {
            assertEquals(0, chargeProductCommand.constructed().size());
        }
    }

    @Test
    public void test_makeTransfer_noIbpa_success() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        String id = customer.makeTransfer(mockAccount, "ID2", 1000.0);
        assertEquals(1, transferCommand.constructed().size());
        verify(transferCommand.constructed().get(0), times(1)).execute();
        assertEquals("TID", id);
    }

    @Test
    public void test_makeTransfer_noIbpa_failure() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        transferCommand.close();
        transferCommand = mockConstruction(TransferCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(false);
        });
        String id = customer.makeTransfer(mockAccount, "ID2", 1000.0);
        assertEquals(1, transferCommand.constructed().size());
        verify(transferCommand.constructed().get(0), times(1)).execute();
        assertNull(id);
    }

    @Test(expected = InvalidAmountException.class)
    public void test_makeTransfer_noIbpa_invalidAmount() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        try {
            customer.makeTransfer(mockAccount, "ID2", -1000.0);
        } finally {
            assertEquals(0, transferCommand.constructed().size());
        }
    }

    @Test(expected = InvalidProductException.class)
    public void test_makeTransfer_noIbpa_invalidProduct() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        try {
            customer.makeTransfer(mockAccount, "ID2", 1000.0);
        } finally {
            assertEquals(0, transferCommand.constructed().size());
        }
    }

    @Test
    public void test_makeTransfer_withIbpa_success() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        String id = customer.makeTransfer(mockAccount, "ID2", "OtherBank", "SWIFT", 1000.0);
        assertEquals(1, transferCommand.constructed().size());
        verify(transferCommand.constructed().get(0), times(1)).execute();
        assertEquals("TID", id);
    }

    @Test
    public void test_makeTransfer_withIbpa_failure() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        transferCommand.close();
        transferCommand = mockConstruction(TransferCommand.class, (mock, context) -> {
            when(mock.execute()).thenReturn(false);
        });
        String id = customer.makeTransfer(mockAccount, "ID2", "OtherBank", "SWIFT", 1000.0);
        assertEquals(1, transferCommand.constructed().size());
        verify(transferCommand.constructed().get(0), times(1)).execute();
        assertNull(id);
    }

    @Test(expected = InvalidAmountException.class)
    public void test_makeTransfer_withIbpa_invalidAmount() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        customer.addProduct(mockAccount);
        try {
            customer.makeTransfer(mockAccount, "ID2", "OtherBank", "SWIFT", -1000.0);
        } finally {
            assertEquals(0, transferCommand.constructed().size());
        }
    }

    @Test(expected = InvalidProductException.class)
    public void test_makeTransfer_withIbpa_invalidProduct() throws Exception {
        when(mockAccount.getId()).thenReturn("ID");
        try {
            customer.makeTransfer(mockAccount, "ID2", "OtherBank", "SWIFT", 1000.0);
        } finally {
            assertEquals(0, transferCommand.constructed().size());
        }
    }

    @Test
    public void test_makeReporting_noFilter() {
        String report = customer.makeReporting();
        assertEquals(1, reportCommand.constructed().size());
        verify(reportCommand.constructed().get(0), times(1)).execute();
        assertEquals("report", report);
    }

    @Test
    public void test_makeReporting_withFilter() {
        String report = customer.makeReporting(p -> true);
        assertEquals(1, reportCommand.constructed().size());
        verify(reportCommand.constructed().get(0), times(1)).execute();
        assertEquals("report", report);
    }

    @Test
    public void test_authorizeTransfer_success() throws Exception {
        when(mockTransferCommand.getId()).thenReturn("TID");
        when(mockTransferCommand.execute()).thenReturn(true);
        customer.addTransferToAuthorize(mockTransferCommand);
        boolean success = customer.authorizeTransfer("TID", "1234");
        assertEquals(0, transferCommand.constructed().size());
        verify(mockTransferCommand, times(1)).execute();
        assertTrue(success);
    }

    @Test
    public void test_authorizeTransfer_failure() throws Exception {
        when(mockTransferCommand.getId()).thenReturn("TID");
        boolean success = customer.authorizeTransfer("TID", "1234");
        assertEquals(0, transferCommand.constructed().size());
        verify(mockTransferCommand, times(0)).execute();
        assertFalse(success);
    }

    @Test
    public void test_accept() {
        when(mockVisitor.visitCustomer(customer)).thenReturn("visited");
        String res = customer.accept(mockVisitor);
        verify(mockVisitor, times(1)).visitCustomer(customer);
        assertEquals("visited", res);
    }

}
