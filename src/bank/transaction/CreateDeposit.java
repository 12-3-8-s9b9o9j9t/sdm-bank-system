package bank.transaction;

import java.time.Period;

import bank.Bank;
import bank.Customer;
import bank.product.account.Account;

class CreateDeposit extends Transaction {

    private Customer owner;
    private Account account;
    private Bank bank;
    private Period period;
    private double amount;

    CreateDeposit(Bank bank, Customer owner, Account account, Period period, double amount) {
        super("createDeposit", "deposit");
        this.account = account;
        this.bank = bank;
    }

    @Override
    public void execute() {
        bank.makeDeposit(owner,account,period,amount);
    }
}