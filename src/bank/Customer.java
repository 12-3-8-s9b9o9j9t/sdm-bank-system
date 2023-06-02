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

    private String ID; // the national ID of the customer
    private String name;
    private Bank bank;
    private List<Product> products = new ArrayList<>();
    private Map<String, AAccount> accounts = new HashMap<>();

    // package-private constructor, only Bank can create Customer
    Customer(String ID, String name, Bank bank) {
        this.ID = ID;
        this.name = name;
        this.bank = bank;
    }

    public String getID() {
        return ID;
    }

    Bank getBank() {
        return bank;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void createAcount() {
        new CreateAccountCommand(bank, this)
            .execute();
    }

    public void createCredit(double limit) {
        new CreateCreditCommand(bank, this, limit)
            .execute();
    }

    public void createLoan(String accountID, Period period, double amount) {
        AAccount account = accounts.get(accountID);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        new CreateLoanCommand(bank, this, account, period, amount)
            .execute();
    }

    public void createDeposit(String accountID, Period period, double amount) {
        AAccount account = accounts.get(accountID);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        new CreateDepositCommand(bank, this, account, period, amount)
            .execute();
    }
    
}
