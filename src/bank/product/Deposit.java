package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.Bank;
import bank.exception.InvalidTransactionException;
import bank.interest.AInterestStrategy;
import bank.interest.FixedInterestStrategy;
import bank.product.account.AAccount;
import bank.transaction.CalculateInterestCommand;
import bank.transaction.ChargeProductCommand;
import bank.transaction.SupplyProductCommand;

public class Deposit extends Product implements ISuppliable {

    private AAccount account;
    private LocalDate targetDate;
    private double limit;
    private double amount = 0;

    public Deposit(String ID, Bank bank, AAccount account, Period period, double limit) {
        super(ID, bank);
        this.targetDate = getCreationDate()
            .plus(period);
        this.account = account;
        this.limit = limit;
        setInterest(new FixedInterestStrategy(AInterestStrategy.HIGH_RATE));
    }

    @Override
    public void supply(double amount) throws InvalidTransactionException {
        if (amount > 0 && this.amount + amount <= limit
                && new ChargeProductCommand(account, amount)
                    .execute()) {
            this.amount += amount;
        } else {
            throw new InvalidTransactionException("supply " + amount, getID());
        }
    }

    public void close() throws InvalidTransactionException {
        new CalculateInterestCommand(this)
            .execute();
        boolean success = new SupplyProductCommand(account, amount)
                .execute();
        if (!success) {
            throw new InvalidTransactionException("close", getID());
        }
        amount = 0;
    }

    @Override
    public double getBalance() {
        return amount;
    }

    @Override
    public void calculateInterest() {
        if (LocalDate.now()
            .isAfter(targetDate)) {
            amount += getInterest()
                .calculate(this);
        }   
    }

}
