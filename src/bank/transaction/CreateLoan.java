package bank.transaction;

import java.time.Period;

import bank.Bank;
import bank.Customer;
import bank.product.account.Account;

class CreateLoan extends Transaction {

    private Customer owner;
    private Account account;
    private Bank bank;
    private Period period;
    private double amount;

    CreateLoan(Bank bank, Customer owner, Account account, Period period, double amount) {
        super("createLoan", "loan");
        this.account = account;
        this.bank = bank;
    }

    @Override
    public void execute() {
        bank.makeLoan(owner,account,period,amount);
    }
}