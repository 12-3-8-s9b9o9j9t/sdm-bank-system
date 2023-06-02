package bank.product;

import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedList;
import java.util.List;

import bank.interest.AInterestState;
import bank.transaction.ATransactionCommand;

public abstract class Product {
    
    private List<ATransactionCommand> history = new LinkedList<>();
    private AInterestState interest_state;
    private double balance;

    public void log(ATransactionCommand transaction) {
        history.add(transaction);
    }

    public void setInterest(AInterestState _state) {
        interest_state = _state;
    }

    public double calculateInterest(LocalDate starDate, LocalDate endDate) {
        return interest_state.calculate(starDate, endDate);
    }

    public double getBalance() {
        return balance;
    }
}
