package bank.product;

import java.util.LinkedList;
import java.util.List;

import bank.interest.Interest;
import bank.transaction.Transaction;

public abstract class Product {
    
    private List<Transaction> history = new LinkedList<>();
    private Interest interest;

    public void log(Transaction transaction) {
        history.add(transaction);
    }

    public void setInterest(Interest interest) {
        this.interest = interest;
    }
}
