package bank.product;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import bank.Bank;
import bank.interest.AInterestStrategy;
import bank.IElement;
import bank.reporter.IVisitor;
import bank.transaction.ATransactionCommand;

public abstract class AProduct implements IElement {
    
    private String id; // given by the bank
    private Bank bank;
    private List<ATransactionCommand> history = new LinkedList<>();
    private LocalDate creationDate = LocalDate.now();
    private AInterestStrategy interestStrategy;

    public AProduct(String id, Bank bank) {
        this.id = id;
        this.bank = bank;
    }

    public AInterestStrategy getInterest() {
        return interestStrategy;
    }

    public void setInterest(AInterestStrategy strategy) {
        interestStrategy = strategy;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public List<ATransactionCommand> getHistory() {
        return history;
    }

    public List<ATransactionCommand> getHistory(LocalDate fromDate) {
        List<ATransactionCommand> result = new LinkedList<>();
        ListIterator<ATransactionCommand> li = history.listIterator(history.size());
        while (li.hasPrevious()) {
            ATransactionCommand transaction = (ATransactionCommand) li.previous();
            if (transaction.getDate()
                .isAfter(fromDate)) {
                result.add(0, transaction);
            }
            else {
                break;
            }
        }
        return result;
    }

    protected void setHistory(List<ATransactionCommand> history) {
        this.history = history;
    }

    public String getId() {
        return id;
    }

    protected Bank getBank() {
        return bank;
    }

    public abstract double getBalance();

    public void log(ATransactionCommand transaction) {
        bank.log(transaction);
        history.add(transaction);
    }
    
    public abstract double calculateInterest();

    public abstract String accept(IVisitor visitor);
}
