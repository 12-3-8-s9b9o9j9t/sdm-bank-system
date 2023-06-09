package bank.product.account;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidTransactionException;
import bank.interest.AInterestStrategy;
import bank.interest.FixedInterestStrategy;
import bank.reporter.IVisitor;

public class BaseAccount extends AAccount {

    private Customer owner;
    private double balance = 0;

    public BaseAccount(String id, Bank bank, Customer owner) {
        super(id, bank);
        this.owner = owner;
        setInterest(new FixedInterestStrategy(AInterestStrategy.LOW_RATE));
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public Customer getOwner() {
        return this.owner;
    }

    @Override
    public void charge(double amount) throws InvalidTransactionException {
        if (amount <= 0 || this.balance < amount) {
            throw new InvalidTransactionException("charge " + amount + "for product " + getId(), "problematic amount");
        }
        this.balance -= amount;
    }

    @Override
    public void supply(double amount) throws InvalidTransactionException {
        if (amount <= 0) {
            throw new InvalidTransactionException("supply " + amount + "for product " + getId(), "problematic amount");
        }
        this.balance += amount;
    }

    @Override
    public double calculateInterest() {
        double interest = getInterest()
            .calculate(this);
        this.balance += interest;
        return interest;
    }

    @Override
    public String accept(IVisitor visitor) {
        return visitor.visitBaseAccount(this);        
    }
}
