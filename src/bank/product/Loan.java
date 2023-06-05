package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.Bank;
import bank.exception.InvalidTransactionException;
import bank.interest.AInterestStrategy;
import bank.interest.FixedInterestStrategy;
import bank.product.account.AAccount;
import bank.transaction.ChargeProductCommand;
import bank.transaction.SupplyProductCommand;

public class Loan extends Product implements ISuppliable {

    private AAccount account;
    private LocalDate targetDate;
    private double amount;

    public Loan(String ID, Bank bank, AAccount account, Period period, double amount) {
        super(ID, bank);
        this.targetDate = getCreationDate()
            .plus(period);
        this.account = account;
        this.amount = amount;
        new SupplyProductCommand(account, amount)
            .execute();
        setInterest(new FixedInterestStrategy());
    }

    @Override
    public void supply(double amount) throws InvalidTransactionException {
        if (amount > 0 && new ChargeProductCommand(account, amount)
            .execute()) {
            this.amount -= amount;
        }
        else {
            throw new InvalidTransactionException("repay " + amount, getID());
        }
    }

    @Override
    public double getBalance() {
        return amount;
    }

    @Override
    public void calculateInterest() {
        amount += getInterest()
            .calculate(this);
    }
    
}
