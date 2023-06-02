package bank;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bank.product.Product;
import bank.product.account.Account;
import bank.transaction.CreateAccount;
import bank.transaction.CreateCredit;
import bank.transaction.CreateDeposit;
import bank.transaction.CreateLoan;

public class Customer {

    private String ID; // the national ID of the customer
    private String name;
    private Bank bank;
    private List<Product> products = new ArrayList<Product>();
    private Map<String, Account> accounts = new HashMap<String, Account>();

    // package-private constructor, only Bank can create Customer
    Customer(String ID, String name, Bank bank) {
        this.ID = ID;
        this.name = name;
        this.bank = bank;
    }

    Bank getBank() {
        return bank;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void createAcount() {
        new CreateAccount(bank, this)
            .execute();
    }

    public void createCredit(double limit) {
        new CreateCredit(bank, this, limit)
            .execute();
    }

    public void createLoan(String accountID, Period period, double amount) {
        Account account = accounts.get(accountID);
        assert account != null : "Account not found";
        new CreateLoan(bank, this, account, period, amount)
            .execute();
    }

    public void createDeposit(String accountID, Period period, double amount) {
        Account account = accounts.get(accountID);
        assert account != null : "Account not found";
        new CreateDeposit(bank, this, account, period, amount)
            .execute();
    }
    
}
