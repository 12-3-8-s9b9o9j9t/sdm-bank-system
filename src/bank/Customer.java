package bank;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bank.product.Product;
import bank.product.account.AAccount;
import bank.transaction.CreateAccountCommand;
import bank.transaction.CreateCreditCommand;
import bank.transaction.CreateDepositCommand;
import bank.transaction.CreateLoanCommand;

public class Customer {

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

    public void createAcount() {
        new CreateAccountCommand(bank, this)
            .execute();
    }

    public void createCredit(double limit) {
        new CreateCreditCommand(bank, this, limit)
            .execute();
    }

    public void createLoan(AAccount account, Period period, double amount) {
        new CreateLoanCommand(bank, this, account, period, amount)
            .execute();
    }

    public void createDeposit(AAccount account, Period period, double amount) {
        new CreateDepositCommand(bank, this, account, period, amount)
            .execute();
    }

}
