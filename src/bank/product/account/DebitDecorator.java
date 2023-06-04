package bank.product.account;

import bank.exception.OperationNotAffordableException;

public class DebitDecorator extends AAccountDecorator {
    
    private double limit;
    private double overdraft = 0;

    public DebitDecorator(AAccount wrapee, double limit) {
        super(wrapee);
        this.limit = limit;
    }

    @Override
    public double getBalance() {
        return wrapee.getBalance() - overdraft;
    }

    @Override
    public void charge(double amount) throws OperationNotAffordableException {
        double balance = wrapee.getBalance();
        if (balance >= amount) {
            wrapee.charge(amount);
        } else if (balance + limit >= amount + overdraft) {
            overdraft += amount - balance;
            wrapee.charge(balance);
        } else {
            throw new OperationNotAffordableException("charge " + amount, getID());
        }
    }

    @Override
    public void supply(double amount) {
        if (overdraft == 0) {
            wrapee.supply(amount);
        } else if (amount <= overdraft) {
            overdraft -= amount;
        } else {
            wrapee.supply(amount - overdraft);
            overdraft = 0;
        }
    }

}
