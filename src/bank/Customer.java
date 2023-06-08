package bank;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import bank.exception.InvalidAmountException;
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

    private int ID;
    private String name;
    private int password;
    private Bank bank;
    private Map<String, Product> products = new HashMap<>();
    private Map<String, TransferCommand> toAuthorize = new HashMap<>();

    // package-private constructor, only Bank can create Customer
    Customer(int ID, String name, String password, Bank bank) {
        this.ID = ID;
        this.name = name;
        this.bank = bank;
        this.password = password.hashCode();
    }

    public int getID() {
        return ID;
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

    public Product getProduct(String ID) {
        return products.get(ID);
    }

    public void addProduct(Product product) {
        products.put(product.getID(), product);
    }

    public boolean removeProduct(Product product) {
        return products.remove(product.getID()) != null;
    }

    public void addTransferToAuthorize(TransferCommand transfer) {
        toAuthorize.put(transfer.getID(), transfer);
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

    public boolean createLoan(AAccount account, Period period, double amount) throws InvalidAmountException, InvalidProductException{
        checkAmount(amount);
        checkProduct(account);
        return new CreateLoanCommand(bank, this, account, period, amount)
            .execute();
    }

    public boolean createDeposit(AAccount account, Period period, double amount) throws InvalidAmountException, InvalidProductException {
        checkAmount(amount);
        checkProduct(account);
        return new CreateDepositCommand(bank, this, account, period, amount)
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
            return transfer.getID();
        }
        return null;
    }

    public String makeTransfert(AAccount sendingAccount, String receivingAccountID, String bankID, String IBPAName, double amount) throws InvalidAmountException, InvalidProductException {
        checkAmount(amount);
        checkProduct(sendingAccount);
        TransferCommand transfer = new TransferCommand(bank, this, sendingAccount, receivingAccountID, bankID, IBPAName, amount);
        if (transfer.execute()) {
            return transfer.getID();
        }
        return null;
    }

    public String makeReporting() {
        return makeReporting(null);
    }

    public String makeReporting(Predicate<Product> filter) {
        String buffer = null;
        new ReportCommand(buffer, filter, this)
            .execute();
        return buffer;
    }


    public boolean authorizeTransfert(String transferID, String password) {
        TransferCommand transfer = toAuthorize.get(transferID);
        if (transfer != null && this.password == password.hashCode()) {
            return transfer.execute();
        }
        return false;
    }

    private void checkProduct(Product product) throws InvalidProductException {
        if(!products.containsKey(product.getID())) {
            throw new InvalidProductException(product.getID());
        }
    }

    private void checkAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
    }

    @Override
    public String accept(IVisitor visitor) {
        return visitor.visitCustomer(this);
    }
    
}
