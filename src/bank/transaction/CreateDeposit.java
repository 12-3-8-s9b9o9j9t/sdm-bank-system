package bank.transaction;

import java.time.Period;

import bank.Bank;
import bank.Customer;
import bank.product.Product;
import bank.product.account.Account;

public class CreateDeposit extends Transaction {

    private Customer owner;
    private Account account;
    private Bank bank;
    private Period period;
    private double amount;

    public CreateDeposit(Bank bank, Customer owner, Account account, Period period, double amount) {
        super(CreateDeposit.class.getSimpleName(), "deposit");
        this.account = account;
        this.bank = bank;
    }

    @Override
    public void execute() {
        Product deposit = bank.createDeposit(owner, account, period, amount);
        log(bank, deposit);
    }
}