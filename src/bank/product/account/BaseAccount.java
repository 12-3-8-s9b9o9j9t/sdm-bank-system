package bank.product.account;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidTransactionException;
import bank.interest.AInterestState;

public class BaseAccount extends AAccount {

    private Customer owner;
    private double balance = 0;

    public BaseAccount(String ID, Bank bank, Customer owner) {
        super(ID, bank);
        this.owner = owner;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public void charge(double amount) throws InvalidTransactionException {
        if (amount <= 0 || this.balance < amount) {
            throw new InvalidTransactionException("charge " + amount, getID());
        }
        this.balance -= amount;
    }

    @Override
    public void supply(double amount) throws InvalidTransactionException {
        if (amount <= 0) {
            throw new InvalidTransactionException("supply " + amount, getID());
        }
        this.balance += amount;
    }

}
