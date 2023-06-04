package bank.product;

import bank.Bank;
import bank.exception.OperationNotAffordableException;

public class Credit extends Product {
    
    private double limit;
    private double amount;

    public Credit(String ID, Bank bank,  double limit) {
        super(ID, bank);
        this.limit = limit;
    }

    public void borrow(double amount) throws OperationNotAffordableException {
        if (this.amount + amount <= limit) {
            this.amount += amount;
        } else {
            throw new OperationNotAffordableException("borrow " + amount, getID());
        }
    }

    public void repay(double amount) {
        this.amount -= amount;
    }

}
