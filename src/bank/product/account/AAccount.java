package bank.product.account;

import bank.Bank;
import bank.exception.OperationNotAffordableException;
import bank.product.Product;

public abstract class AAccount extends Product {

    public AAccount(String ID, Bank bank) {
        super(ID, bank);
    }

    protected Bank getBank() {
        return super.getBank();
    }

    abstract public double getBalance();

    abstract public void charge(double amount) throws OperationNotAffordableException;

    abstract public void supply(double amount);

}
