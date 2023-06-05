package bank;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

import bank.exception.InvalidAmountException;
import bank.exception.InvalidProductException;
import bank.product.Deposit;
import bank.product.IChargeable;
import bank.product.ISuppliable;
import bank.product.Product;
import bank.product.account.AAccount;
import bank.reporter.IVisitor;
import bank.transaction.CloseDepositCommand;
import bank.transaction.CreateAccountCommand;
import bank.transaction.CreateCreditCommand;
import bank.transaction.CreateDepositCommand;
import bank.transaction.CreateLoanCommand;
import bank.transaction.ExtendAccountWithDebitCommand;

public class Customer implements IElement {

    private int ID;
    private String name;
    private Bank bank;
    private Map<String, Product> products = new HashMap<>();

    // package-private constructor, only Bank can create Customer
    Customer(int ID, String name, Bank bank) {
        this.ID = ID;
        this.name = name;
        this.bank = bank;
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

    public void createAcount() {
        new CreateAccountCommand(bank, this)
            .execute();
    }

    public void createCredit(double limit) {
        checkAmount(limit);
        new CreateCreditCommand(bank, this, limit)
            .execute();
    }

    public void createLoan(AAccount account, Period period, double amount) {
        checkAmount(amount);
        checkProduct(account);
        new CreateLoanCommand(bank, this, account, period, amount)
            .execute();
    }

    public void createDeposit(AAccount account, Period period, double amount) {
        checkAmount(amount);
        checkProduct(account);
        new CreateDepositCommand(bank, this, account, period, amount)
            .execute();
    }

    public void extendAccountWithDebit(AAccount account, double limit) {
        checkAmount(limit);
        checkProduct(account);
        new ExtendAccountWithDebitCommand(bank, this, account, limit)
            .execute();
    }

    public void closeDeposit(Deposit deposit) {
        checkProduct(deposit);
        boolean success = new CloseDepositCommand(deposit)
                .execute();
        if (success) {
            removeProduct(deposit);
        }
    }

    public void supplyProduct(ISuppliable supplied, double amount) {
        checkAmount(amount);
        Product product = (Product)supplied;
        checkProduct(product);
    }

    public void chargeProduct(IChargeable charged, double amount) {
        checkAmount(amount);
        Product product = (Product)charged;
        checkProduct(product);
    }

    public void makeTransfert(AAccount sender, String receiverID, double amount) {
        checkAmount(amount);
        checkProduct(sender);
        // TODO
    }

    public void makeTransfert(AAccount sender, String receiverID, String bankID, double amount) {
        checkAmount(amount);
        checkProduct(sender);
        // TODO
    }

    private void checkProduct(Product product) {
        if(!products.containsKey(product.getID())) {
            throw new InvalidProductException(product.getID());
        }
    }

    private void checkAmount(double amount) {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
    }

    @Override
    public String accept(IVisitor visitor) {
        return visitor.visitCustomer(this);
    }
    
}
