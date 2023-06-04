package bank.product;

import bank.exception.InvalidTransactionException;

public interface ISuppliable {
    public void supply(double amount) throws InvalidTransactionException;
}
