package bank.product;

import bank.Bank;
import bank.exception.InvalidTransactionException;
import bank.interest.HistoryBasedStrategy;
import bank.reporter.IVisitor;

public class Credit extends AProduct implements IChargeable, ISuppliable {
    
    private double limit;
    private double amount;

    public Credit(String id, Bank bank,  double limit) {
        super(id, bank);
        this.limit = limit;
        setInterest(new HistoryBasedStrategy(false));
    }

    public double getLimit() {
        return limit;
    }

    @Override
    public void charge(double amount) throws InvalidTransactionException {
        if (amount > 0 && this.amount + amount <= limit) {
            this.amount += amount;
        } else {
            throw new InvalidTransactionException("borrow " + amount + "for product " + getId(), "problematic amount");
        }
    }

    @Override
    public void supply(double amount) throws InvalidTransactionException {
        if (amount <= 0) {
            throw new InvalidTransactionException("repay " + amount + "for product " + getId(), "problematic amount");
        }
        this.amount -= amount;
    }

    @Override
    public double getBalance() {
        return amount;
    }

    @Override
    public double calculateInterest() {
        double interest = getInterest()
            .calculate(this);
        amount += interest;
        return -interest;
    }

    @Override
    public String accept(IVisitor visitor) {
        return visitor.visitCredit(this);
    }
}
