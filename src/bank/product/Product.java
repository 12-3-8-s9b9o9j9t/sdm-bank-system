package bank.product;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;

import bank.interest.Interest;
import bank.transaction.Transaction;

public abstract class Product {
    
    private List<Transaction> history = new LinkedList<>();
    private Interest interest_state;
    private double balance;

    public void log(Transaction transaction) {
        history.add(transaction);
    }

    public void setInterest(Interest _state) {
        interest_state = _state;
    }

    public double calculateInterest(LocalDate starDate, LocalDate endDate) {
        return interest_state.calculate(starDate, endDate);
    }

    public double getBalance() {
        return balance;
    }
}
