package bank.product;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import bank.Bank;
import bank.interest.AInterestState;
import bank.transaction.ATransactionCommand;

public abstract class Product {
    
    private String ID; // given by the bank
    private Bank bank;
    private List<ATransactionCommand> history = new LinkedList<>();
    private LocalDate creationDate = LocalDate.now();
    private AInterestState interestState;

    public Product(String ID, Bank bank) {
        this.ID = ID;
        this.bank = bank;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public List<ATransactionCommand> getHistory() {
        return history;
    }

    protected void setHistory(List<ATransactionCommand> history) {
        this.history = history;
    }

    public String getID() {
        return ID;
    }

    protected Bank getBank() {
        return bank;
    }

    public void log(ATransactionCommand transaction) {
        bank.log(transaction);
        history.add(transaction);
    }

    public void setInterest(AInterestState state) {
        interestState = state;
    }

    /*
    public double calculateInterest(LocalDate starDate, LocalDate endDate) {
        return interestState.calculate(starDate, endDate);
    }*/
}
