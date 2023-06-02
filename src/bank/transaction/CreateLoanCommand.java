package bank.transaction;

import java.time.Period;

import bank.Bank;
import bank.Customer;
import bank.product.Product;
import bank.product.account.AAccount;

public class CreateLoanCommand extends ATransactionCommand {

    private Customer owner;
    private AAccount account;
    private Bank bank;
    private Period period;
    private double amount;

    public CreateLoanCommand(Bank bank, Customer owner, AAccount account, Period period, double amount) {
        super(CreateLoanCommand.class.getSimpleName(), "loan");
        this.account = account;
        this.bank = bank;
    }

    @Override
    public void execute() {
        Product loan = bank.createLoan(owner,account,period,amount);
        log(bank, loan);
    }
}