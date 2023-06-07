package bank.transaction;

import java.time.Period;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidCustomerException;
import bank.product.Product;
import bank.product.account.AAccount;

public class CreateDepositCommand extends ATransactionCommand {

    private Customer owner;
    private AAccount account;
    private Bank bank;
    private Period period;
    private double limit;

    public CreateDepositCommand(Bank bank, Customer owner, AAccount account, Period period, double limit) {
        super("Create Deposit", "Creating Deposit");
        this.account = account;
        this.bank = bank;
    }

    @Override
    public boolean execute() {
        try {
            Product deposit = bank.createDeposit(owner, account, period, limit);
            deposit.log(this);
            return true;
        } catch (InvalidCustomerException e) {
            return false;
        }
    }

    @Override
    public double getValue() {
        return 0;
    }
}