package bank.transaction;

import java.time.Period;

import bank.Bank;
import bank.Customer;
import bank.product.Product;
import bank.product.account.AAccount;

public class CreateDepositCommand extends ATransactionCommand {

    private Customer owner;
    private AAccount account;
    private Bank bank;
    private Period period;
    private double amount;

    public CreateDepositCommand(Bank bank, Customer owner, AAccount account, Period period, double amount) {
        super(CreateDepositCommand.class.getSimpleName(), "deposit");
        this.account = account;
        this.bank = bank;
    }

    @Override
    public void execute() {
        Product deposit = bank.createDeposit(owner, account, period, amount);
        log(bank, deposit);
    }
}