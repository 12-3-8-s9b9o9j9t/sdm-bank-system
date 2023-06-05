package bank.product;

import bank.Bank;
import bank.exception.InvalidTransactionException;
import bank.interest.HistoryBasedStrategy;
import bank.reporter.IVisitor;

public class Credit extends Product implements IChargeable, ISuppliable {
    
    private double limit;
    private double amount;

    public Credit(String ID, Bank bank,  double limit) {
        super(ID, bank);
        this.limit = limit;
        setInterest(new HistoryBasedStrategy());
    }

    public double getLimit() {
        return limit;
    }

    @Override
    public void charge(double amount) throws InvalidTransactionException {
        if (amount > 0 && this.amount + amount <= limit) {
            this.amount += amount;
        } else {
            throw new InvalidTransactionException("borrow " + amount + "for product " + getID(), "problematic amount");
        }
    }

    @Override
    public void supply(double amount) throws InvalidTransactionException {
        if (amount <= 0) {
            throw new InvalidTransactionException("repay " + amount + "for product " + getID(), "problematic amount");
        }
        this.amount -= amount;
    }

    @Override
    public double getBalance() {
        return amount;
    }

    @Override
    public void calculateInterest() {
        amount += getInterest()
            .calculate(this);   
    }

    @Override
    public String accept(IVisitor visitor) {
        return visitor.visitCredit(this);
    }
}
