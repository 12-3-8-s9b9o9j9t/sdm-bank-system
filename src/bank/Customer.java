package bank;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bank.product.Product;
import bank.product.account.AAccount;
import bank.transaction.CloseAccountCommand;
import bank.transaction.CreateAccountCommand;
import bank.transaction.CreateCreditCommand;
import bank.transaction.CreateDepositCommand;
import bank.transaction.CreateLoanCommand;
import bank.transaction.PayCommand;
import bank.transaction.TransferCommand;
import bank.transaction.WithdrawCommand;

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

<<<<<<< HEAD
=======
    public void createWithdraw(String accountID, Period period, double amount) {
        AAccount account = accounts.get(accountID);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        //Can't do a withdrow of 0 or negative value
        if (amount == 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (amount<= 0) {
            throw new RuntimeException("Invalid amount");
        }

        //If everything is clear, we call the command
        new WithdrawCommand(bank, account, period, amount, this)
                .execute();
    }

    public void createPayment(String accountID, Period period, double amount, Card card) {
        AAccount account = accounts.get(accountID);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        //Can't do a payment of 0 or negative value
        if (amount == 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (amount<= 0) {
            throw new RuntimeException("Invalid amount");
        }

        //If everything is clear, we call the command
        new PayCommand(bank, account, period, amount, this, card)
                .execute();
    }

    //Make Transfert
    public void createTransfert(String accountID, AAccount accountIDrecept, Period period, double amount) {
        AAccount account = accounts.get(accountID);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }
        if (accountIDrecept == null) {
            throw new RuntimeException("Account not found");
        }

        //Can't do a payment of 0 or negative value
        if (amount == 0) {
            throw new RuntimeException("Invalid amount");
        }

        if (amount<= 0) {
            throw new RuntimeException("Invalid amount");
        }

        //If everything is clear, we call the command
        new TransferCommand(bank, account, accountIDrecept, period, amount, this)
                .execute();
    }

    //Delete Account
    public void DeleteAccoun(String accountID) {
        AAccount account = accounts.get(accountID);
        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        //If everything is clear, we call the command
        new CloseAccountCommand(bank, account, accountID)
                .execute();
    }


    
>>>>>>> transaction
}
