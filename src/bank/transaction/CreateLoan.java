package bank.transaction;

import java.time.Period;

import bank.Bank;
import bank.Customer;
import bank.product.Product;
import bank.product.account.Account;

public class CreateLoan extends Transaction {

    private Customer owner;
    private Account account;
    private Bank bank;
    private Period period;
    private double amount;

    public CreateLoan(Bank bank, Customer owner, Account account, Period period, double amount) {
        super(CreateLoan.class.getSimpleName(), "loan");
        this.account = account;
        this.bank = bank;
    }

    @Override
    public void execute() {
        Product loan = bank.createLoan(owner,account,period,amount);
        log(bank, loan);
    }
}