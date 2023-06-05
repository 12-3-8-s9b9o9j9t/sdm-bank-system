package bank.product.account;

import bank.exception.InvalidTransactionException;
import bank.reporter.IVisitor;

public class DebitDecorator extends AAccountDecorator {
    
    private double limit;
    private double overdraft = 0;

    public DebitDecorator(AAccount wrapee, double limit) {
        super(wrapee);
        this.limit = limit;
    }

    public double getOverdraft() {
        return overdraft;
    }

    @Override
    public double getBalance() {
        return wrapee.getBalance() - overdraft;
    }

    @Override
    public void charge(double amount) throws InvalidTransactionException {
        if (amount <= 0) {
            throw new InvalidTransactionException("charge " + amount, getID());
        }
        double balance = wrapee.getBalance();
        if (balance >= amount) {
            wrapee.charge(amount);
        } else if (balance + limit >= amount + overdraft) {
            overdraft += amount - balance;
            wrapee.charge(balance);
        } else {
            throw new InvalidTransactionException("charge " + amount, getID());
        }
    }

    @Override
    public void supply(double amount) throws InvalidTransactionException {
        if (amount <= 0) {
            throw new InvalidTransactionException("supply " + amount, getID());
        }
        if (overdraft == 0) {
            wrapee.supply(amount);
        } else if (amount <= overdraft) {
            overdraft -= amount;
        } else {
            wrapee.supply(amount - overdraft);
            overdraft = 0;
        }
    }

    @Override
    public void calculateInterest() {
        wrapee.calculateInterest();
    }

    @Override
    public String accept(IVisitor visitor) {
        return visitor.visitDebitAccount(this);
    }
}