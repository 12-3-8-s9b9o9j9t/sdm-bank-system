package bank.product;

import bank.Bank;
import bank.exception.InvalidTransactionException;

public class Credit extends Product implements IChargeable, ISuppliable {
    
    private double limit;
    private double amount;

    public Credit(String ID, Bank bank,  double limit) {
        super(ID, bank);
        this.limit = limit;
    }

    @Override
    public void charge(double amount) throws InvalidTransactionException {
        if (amount > 0 && this.amount + amount <= limit) {
            this.amount += amount;
        } else {
            throw new InvalidTransactionException("borrow " + amount, getID());
        }
    }

    @Override
    public void supply(double amount) throws InvalidTransactionException {
        if (amount <= 0) {
            throw new InvalidTransactionException("repay " + amount, getID());
        }
        this.amount -= amount;
    }

}
