package bank.transaction;

import java.time.LocalDate;

import bank.Bank;
import bank.product.Product;

public abstract class Transaction {

    private String type;
    private LocalDate date = LocalDate.now();
    private String description;

    public Transaction(String type, String description) {
        this.type = type;
        this.description = description;
    }

    abstract public void execute();

    protected void log(Bank bank, Product product) {
        bank.log(this);
        product.log(this);
    }
    
}
