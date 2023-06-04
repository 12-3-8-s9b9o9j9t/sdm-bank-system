package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.Bank;
import bank.exception.OperationNotAffordableException;
import bank.product.account.AAccount;

public class Deposit extends Product {

    private AAccount account;
    private LocalDate targetDate;
    private double limit;
    private double amount = 0;

    public Deposit(String ID, Bank bank, AAccount account, Period period, double limit) {
        super(ID, bank);
        this.targetDate = getCreationDate().plus(period);
        this.account = account;
        this.limit = limit;
    }

    public void supply(double amount) throws OperationNotAffordableException {
        if (this.amount + amount <= limit) {
            this.amount += amount;
        } else {
            throw new OperationNotAffordableException("supply " + amount, getID());
        }
    }

    public void close() {
        // TODO Auto-generated method stub
    }

}
