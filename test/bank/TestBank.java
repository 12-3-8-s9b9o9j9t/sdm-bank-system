package bank;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;

import bank.exception.BankAlreadyRegisteredAtIbpaException;
import bank.exception.CustomerAlreadyExistsException;
import bank.exception.InvalidAmountException;
import bank.exception.InvalidCustomerException;
import bank.exception.InvalidPeriodException;
import bank.exception.InvalidProductException;
import bank.ibpa.InterBankPaymentAgencyMediator;
import bank.interest.AInterestStrategy;
import bank.product.Credit;
import bank.product.Deposit;
import bank.product.Loan;
import bank.product.Product;
import bank.product.account.AAccount;
import bank.product.account.BaseAccount;
import bank.product.account.DebitDecorator;
import bank.transaction.CalculateInterestCommand;
import bank.transaction.ChangeInterestCommand;
import bank.transaction.transfer.TransferCommand;

public class TestBank {

    private Bank bank = null;
    private Bank bank2 = null;
    private InterBankPaymentAgencyMediator mockIbpa = null;
    private TransferCommand mockTransfer = null;
    private TransferCommand mockTransfer2 = null;
    private Customer mockCustomer = null;
    private Customer mockCustomer2 = null;
    private Product mockProduct = null;
    private AAccount mockAccount = null;
    private AAccount mockAccount2 = null;
    private AInterestStrategy mockInterest = null;
    private MockedConstruction<Customer> customer = null;
    private MockedConstruction<BaseAccount> baseAccount = null;
    private MockedConstruction<Credit> credit = null;
    private MockedConstruction<Loan> loan = null;
    private MockedConstruction<Deposit> deposit = null;
    private MockedConstruction<DebitDecorator> debitDecorator = null;
    private MockedConstruction<ChangeInterestCommand> changeInterestCommand = null;
    private MockedConstruction<CalculateInterestCommand> calculateInterestCommand = null;


    @Before
    public void setUp() {
        bank = new Bank("TestBANK");
        bank2 = new Bank("OtherBANK");
        mockIbpa = mock(InterBankPaymentAgencyMediator.class);
        mockTransfer = mock(TransferCommand.class);
        mockTransfer2 = mock(TransferCommand.class);
        mockCustomer = mock(Customer.class);
        mockCustomer2 = mock(Customer.class);
        mockProduct = mock(Product.class);
        mockAccount = mock(AAccount.class);
        mockAccount2 = mock(AAccount.class);
        mockInterest = mock(AInterestStrategy.class);
        customer = mockConstruction(Customer.class);
        baseAccount = mockConstruction(BaseAccount.class);
        credit = mockConstruction(Credit.class);
        loan = mockConstruction(Loan.class);
        deposit = mockConstruction(Deposit.class);
        debitDecorator = mockConstruction(DebitDecorator.class);
        changeInterestCommand = mockConstruction(ChangeInterestCommand.class);
        calculateInterestCommand = mockConstruction(CalculateInterestCommand.class);
    }

    @After
    public void tearDown() {
        bank = null;
        bank2 = null;
        mockIbpa = null;
        mockTransfer = null;
        mockTransfer2 = null;
        mockCustomer = null;
        mockCustomer2 = null;
        mockProduct = null;
        mockAccount = null;
        mockAccount2 = null; 
        mockInterest = null;
        customer.close();
        baseAccount.close();
        credit.close();
        loan.close();
        deposit.close();
        debitDecorator.close();
        changeInterestCommand.close();
        calculateInterestCommand.close();
    }

    @Test
    public void test_registerAtIbpa() throws Exception {
        when(mockIbpa.getName()).thenReturn("TestIbpa");
        bank.registerAtIbpa(mockIbpa);
        verify(mockIbpa, times(1)).notify(bank, "register");
    }

    @Test(expected = BankAlreadyRegisteredAtIbpaException.class)
    public void test_registerAtIbpa_alreadyRegistered() throws Exception {
        when(mockIbpa.getName()).thenReturn("TestIbpa");
        doAnswer(invocation -> {
            bank.addIbpa("TESTBANK0", mockIbpa);
            return null;
        }).when(mockIbpa).notify(bank, "register");
        bank.registerAtIbpa(mockIbpa);
        bank.registerAtIbpa(mockIbpa);
    }

    @Test
    public void test_addPendingTransfer_insideNoIbpa() throws Exception {
        when(mockTransfer.getReceivingBankID()).thenReturn(null);
        bank.addPendingTransfer(mockTransfer);
        Field f = bank.getClass().getDeclaredField("insidePendingTransfers");
        f.setAccessible(true);
        List<TransferCommand> transfers = (List<TransferCommand>) f.get(bank);
        assertEquals(1, transfers.size());
    }

    @Test
    public void test_addPendingTransfer_insideIbpa() throws Exception {
        when(mockIbpa.getName()).thenReturn("TestIbpa");
        when(mockTransfer.getReceivingBankID()).thenReturn("TESTBANK0");
        when(mockTransfer.getIbpaName()).thenReturn("TestIbpa");
        bank.addIbpa("TESTBANK0", mockIbpa);
        bank.addPendingTransfer(mockTransfer);
        Field f = bank.getClass().getDeclaredField("insidePendingTransfers");
        f.setAccessible(true);
        List<TransferCommand> transfers = (List<TransferCommand>) f.get(bank);
        assertEquals(1, transfers.size());
    }

    @Test
    public void test_addPendingTransfer_outside() throws Exception {
        when(mockIbpa.getName()).thenReturn("TestIbpa");
        when(mockTransfer.getReceivingBankID()).thenReturn("OTHERBANK0");
        when(mockTransfer.getIbpaName()).thenReturn("TestIbpa");
        bank.addIbpa("TESTBANK0", mockIbpa);
        bank.addPendingTransfer(mockTransfer);
        List<TransferCommand> transfers = bank.getPendingTransfers("TestIbpa");
        assertEquals(1, transfers.size());
    }

    @Test
    public void test_registerCustomer() throws Exception {
        Customer c = bank.registerCustomer("123456", "John Doe", "password");
        assertEquals(1, customer.constructed().size());
        assertEquals(c, customer.constructed().get(0));
    }

    @Test(expected = CustomerAlreadyExistsException.class)
    public void test_registerCustomer_alreadyExists() throws Exception {
        try {
            bank.registerCustomer("123456", "John Doe", "password");
            bank.registerCustomer("123456", "Jane Doe", "qwerty");
        } finally {
            assertEquals(1, customer.constructed().size());
        }
    }

    @Test
    public void test_createAccount() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        Product a = bank.createAccount(mockCustomer);
        assertEquals(1, baseAccount.constructed().size());
        assertEquals(a, baseAccount.constructed().get(0));
        verify(mockCustomer, times(1)).addProduct(a);
    }

    @Test(expected = InvalidCustomerException.class)
    public void test_createAccount_invalidCustomer() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank2);
        try {
            bank.createAccount(mockCustomer);
        } finally {
            assertEquals(0, baseAccount.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test
    public void test_createCredit() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        Product c = bank.createCredit(mockCustomer, 1000);
        assertEquals(1, credit.constructed().size());
        assertEquals(c, credit.constructed().get(0));
        verify(mockCustomer, times(1)).addProduct(c);
    }

    @Test(expected = InvalidCustomerException.class)
    public void test_createCredit_invalidCustomer() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank2);
        try {
            bank.createCredit(mockCustomer, 1000);
        } finally {
            assertEquals(0, credit.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test(expected = InvalidAmountException.class)
    public void test_createCredit_invalidAmount() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        try {
            bank.createCredit(mockCustomer, -1000);
        } finally {
            assertEquals(0, credit.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test
    public void test_createLoan() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(mockAccount);
        Product l = bank.createLoan(mockCustomer, mockAccount, Period.ofMonths(12), 1000);
        assertEquals(1, loan.constructed().size());
        assertEquals(l, loan.constructed().get(0));
        verify(mockCustomer, times(1)).addProduct(l);
    }

    @Test(expected = InvalidCustomerException.class)
    public void test_createLoan_invalidCustomer() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank2);
        when(mockCustomer.getProduct(anyString())).thenReturn(mockAccount);
        try {
            bank.createLoan(mockCustomer, mockAccount, Period.ofMonths(12), 1000);
        } finally {
            assertEquals(0, loan.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test(expected = InvalidProductException.class)
    public void test_createLoan_invalidAccount() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(null);
        try {
            bank.createLoan(mockCustomer, mockAccount, Period.ofMonths(12), 1000);
        } finally {
            assertEquals(0, loan.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test(expected = InvalidPeriodException.class)
    public void test_createLoan_invalidPeriod() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(mockAccount);
        try {
            bank.createLoan(mockCustomer, mockAccount, Period.ofMonths(0), 1000);
        } finally {
            assertEquals(0, loan.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test(expected = InvalidAmountException.class)
    public void test_createLoan_invalidAmount() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(mockAccount);
        try {
            bank.createLoan(mockCustomer, mockAccount, Period.ofMonths(12), -1000);
        } finally {
            assertEquals(0, loan.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test
    public void test_createDeposit() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(mockAccount);
        Product d = bank.createDeposit(mockCustomer, mockAccount, Period.ofMonths(12), 1000);
        assertEquals(1, deposit.constructed().size());
        assertEquals(d, deposit.constructed().get(0));
        verify(mockCustomer, times(1)).addProduct(d);
    }

    @Test(expected = InvalidCustomerException.class)
    public void test_createDeposit_invalidCustomer() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank2);
        when(mockCustomer.getProduct(anyString())).thenReturn(mockAccount);
        try {
            bank.createDeposit(mockCustomer, mockAccount, Period.ofMonths(12), 1000);
        } finally {
            assertEquals(0, deposit.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test(expected = InvalidProductException.class)
    public void test_createDeposit_invalidAccount() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(null);
        try {
            bank.createDeposit(mockCustomer, mockAccount, Period.ofMonths(12), 1000);
        } finally {
            assertEquals(0, deposit.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test(expected = InvalidPeriodException.class)
    public void test_createDeposit_invalidPeriod() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(mockAccount);
        try {
            bank.createDeposit(mockCustomer, mockAccount, Period.ofMonths(0), 1000);
        } finally {
            assertEquals(0, deposit.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test(expected = InvalidAmountException.class)
    public void test_createDeposit_invalidAmount() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(mockAccount);
        try {
            bank.createDeposit(mockCustomer, mockAccount, Period.ofMonths(12), -1000);
        } finally {
            assertEquals(0, deposit.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test
    public void test_extendAccountWithDebit() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(mockAccount);
        Product d = bank.extendAccountWithDebit(mockCustomer, mockAccount, 1000);
        assertEquals(1, debitDecorator.constructed().size());
        assertEquals(d, debitDecorator.constructed().get(0));
        verify(mockCustomer, times(1)).addProduct(d);
    }

    @Test(expected = InvalidCustomerException.class)
    public void test_extendAccountWithDebit_invalidCustomer() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank2);
        when(mockCustomer.getProduct(anyString())).thenReturn(mockAccount);
        try {
            bank.extendAccountWithDebit(mockCustomer, mockAccount, 1000);
        } finally {
            assertEquals(0, debitDecorator.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test(expected = InvalidProductException.class)
    public void test_extendAccountWithDebit_invalidAccount() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(null);
        try {
            bank.extendAccountWithDebit(mockCustomer, mockAccount, 1000);
        } finally {
            assertEquals(0, debitDecorator.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test(expected = InvalidAmountException.class)
    public void test_extendAccountWithDebit_invalidAmount() throws Exception {
        when(mockCustomer.getBank()).thenReturn(bank);
        when(mockAccount.getId()).thenReturn("ID");
        when(mockCustomer.getProduct("ID")).thenReturn(mockAccount);
        try {
            bank.extendAccountWithDebit(mockCustomer, mockAccount, -1000);
        } finally {
            assertEquals(0, debitDecorator.constructed().size());
            verify(mockCustomer, times(0)).addProduct(any());
        }
    }

    @Test
    public void test_changeInterest() throws Exception {
        when(mockCustomer.getProduct("ID")).thenReturn(mockAccount);
        Field f = bank.getClass().getDeclaredField("customers");
        f.setAccessible(true);
        Map<Integer, Customer> customers = (Map<Integer, Customer>) f.get(bank);
        customers.put(0, mockCustomer);
        bank.changeInterest(0, "ID", mockInterest);
        assertEquals(1, changeInterestCommand.constructed().size());
        verify(changeInterestCommand.constructed().get(0), times(1)).execute();
    }

    @Test
    public void test_calculateInterest() throws Exception {
        when(mockCustomer.getProducts()).thenReturn(Map.of("ID", mockAccount));
        when(mockCustomer2.getProducts()).thenReturn(Map.of("ID2", mockAccount2, "ID3", mockProduct));
        Field f = bank.getClass().getDeclaredField("customers");
        f.setAccessible(true);
        Map<Integer, Customer> customers = (Map<Integer, Customer>) f.get(bank);
        customers.put(0, mockCustomer);
        customers.put(1, mockCustomer2);
        bank.calculateInterest();
        verify(mockCustomer, times(1)).getProducts();
        verify(mockCustomer2, times(1)).getProducts();
        assertEquals(3, calculateInterestCommand.constructed().size());
        calculateInterestCommand.constructed().forEach(c -> verify(c, times(1)).execute());
   }

   @Test
   public void test_executeTransfers() throws Exception {
        when(mockTransfer.getReceivingAccountID()).thenReturn("ID");
        when(mockTransfer2.getReceivingAccountID()).thenReturn("ID2");
        Field f = bank.getClass().getDeclaredField("accounts");
        f.setAccessible(true);
        f.set(bank, Map.of("ID", mockAccount, "ID2", mockAccount2));
        f = bank.getClass().getDeclaredField("insidePendingTransfers");
        f.setAccessible(true);
        List<TransferCommand> transfers = new LinkedList<>(List.of(mockTransfer, mockTransfer2));
        f.set(bank, transfers);
        bank.executeTransfers();
        verify(mockTransfer, times(1)).execute();
        verify(mockTransfer2, times(1)).execute();
        assertEquals(0, transfers.size());
   }

}
