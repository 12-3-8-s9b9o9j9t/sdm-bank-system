package bank.product;

import bank.exception.InvalidTransactionException;

public interface IChargeable {
    public void charge(double amount) throws InvalidTransactionException;
}
