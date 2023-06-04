package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.Bank;
import bank.exception.InvalidTransactionException;
import bank.product.account.AAccount;
import bank.transaction.ChargeProductCommand;

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

    public void close() {
        // TODO Auto-generated method stub
    }

}
