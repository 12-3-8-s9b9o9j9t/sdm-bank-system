package bank;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

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

public class Customer implements IElement {

    private int id;
    private String name;
    private int password;
    private Bank bank;
    private Map<String, Product> products = new HashMap<>();
    private Map<String, TransferCommand> toAuthorize = new HashMap<>();

    // package-private constructor, only Bank can create Customer
    Customer(int id, String name, String password, Bank bank) {
        this.id = id;
        this.name = name;
        this.bank = bank;
        this.password = password.hashCode();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    Bank getBank() {
        return bank;
    }

    public Map<String, Product> getProducts() {
        return products;
    }

    public Product getProduct(String id) {
        return products.get(id);
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public boolean removeProduct(Product product) {
        return products.remove(product.getId()) != null;
    }

    public void addTransferToAuthorize(TransferCommand transfer) {
        toAuthorize.put(transfer.getId(), transfer);
    }

    public boolean createAcount() {
        return new CreateAccountCommand(bank, this)
            .execute();
    }

    public boolean createCredit(double limit) throws InvalidAmountException {
        checkAmount(limit);
        return new CreateCreditCommand(bank, this, limit)
            .execute();
    }

    public boolean createLoan(AAccount account, Period period, double amount) throws InvalidAmountException, InvalidProductException, InvalidPeriodException {
        checkAmount(amount);
        checkProduct(account);
        checkPeriod(period.normalized());
        return new CreateLoanCommand(bank, this, account, period.normalized(), amount)
            .execute();
    }

    public boolean createDeposit(AAccount account, Period period, double amount) throws InvalidAmountException, InvalidProductException, InvalidPeriodException {
        checkAmount(amount);
        checkProduct(account);
        checkPeriod(period.normalized());
        return new CreateDepositCommand(bank, this, account, period.normalized(), amount)
            .execute();
    }

    public boolean extendAccountWithDebit(AAccount account, double limit) throws InvalidAmountException, InvalidProductException {
        checkAmount(limit);
        checkProduct(account);
        return new ExtendAccountWithDebitCommand(bank, this, account, limit)
            .execute();
    }

    public boolean closeDeposit(Deposit deposit) throws InvalidProductException {
        checkProduct(deposit);
        boolean success = new CloseDepositCommand(deposit)
                .execute();
        if (success) {
            removeProduct(deposit);
        }
        return success;
    }

    public boolean supplyProduct(ISuppliable supplied, double amount) throws InvalidAmountException, InvalidProductException {
        checkAmount(amount);
        Product product = (Product)supplied;
        checkProduct(product);
        return new SupplyProductCommand(supplied, amount)
            .execute();
    }

    public boolean chargeProduct(IChargeable charged, double amount) throws InvalidAmountException, InvalidProductException {
        checkAmount(amount);
        Product product = (Product)charged;
        checkProduct(product);
        return new ChargeProductCommand(charged, amount)
            .execute();
    }

    public String makeTransfert(AAccount sendingAccount, String receivingAccountID, double amount) throws InvalidAmountException, InvalidProductException {
        checkAmount(amount);
        checkProduct(sendingAccount);
        TransferCommand transfer = new TransferCommand(bank, this, sendingAccount, receivingAccountID, amount);
        if (transfer.execute()) {
            return transfer.getId();
        }
        return null;
    }

    public String makeTransfert(AAccount sendingAccount, String receivingAccountID, String bankID, String IbpaName, double amount) throws InvalidAmountException, InvalidProductException {
        checkAmount(amount);
        checkProduct(sendingAccount);
        TransferCommand transfer = new TransferCommand(bank, this, sendingAccount, receivingAccountID, bankID, IbpaName, amount);
        if (transfer.execute()) {
            return transfer.getId();
        }
        return null;
    }

    public String makeReporting() {
        return makeReporting(null);
    }

    public String makeReporting(Predicate<Product> filter) {
        StringBuilder builder = new StringBuilder();
        new ReportCommand(builder, filter, this)
            .execute();
        return builder.toString();
    }


    public boolean authorizeTransfert(String transferID, String password) {
        TransferCommand transfer = toAuthorize.get(transferID);
        if (transfer != null && this.password == password.hashCode()) {
            return transfer.execute();
        }
        return false;
    }

    private void checkProduct(Product product) throws InvalidProductException {
        if(!products.containsKey(product.getId())) {
            throw new InvalidProductException(product.getId());
        }
    }

    private static void checkAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
    }

    private static void checkPeriod(Period period) throws InvalidPeriodException {
        if (period.isNegative() || period.isZero()) {
            throw new InvalidPeriodException();
        }
    }

    @Override
    public String accept(IVisitor visitor) {
        return visitor.visitCustomer(this);
    }
    
}
