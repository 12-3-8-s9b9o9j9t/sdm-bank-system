package bank.transaction;

import java.time.Period;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidInputException;
import bank.product.Deposit;
import bank.product.account.AAccount;

public class CreateDepositCommand extends ATransactionCommand {

    private Customer owner;
    private AAccount account;
    private Bank bank;
    private Period period;
    private double limit;

    public CreateDepositCommand(Bank bank, Customer owner, AAccount account, Period period, double limit) {
        super("Create Deposit", 
            new StringBuilder("Creating Deposit limited to ")
            .append(limit)
            .append(" linked to Account ")
            .append(account.getId())
            .append(" to not be closed before ")
            .append(period.toString()).toString());
        this.account = account;
        this.bank = bank;
        this.owner = owner;
        this.period = period;
        this.limit = limit;
    }

    @Override
    public boolean execute() {
        try {
            Deposit deposit = bank.createDeposit(owner, account, period, limit);
            deposit.log(this);
            return true;
        } catch (InvalidInputException e) {
            return false;
        }
    }

    @Override
    public double getValue() {
        return 0;
    }
}