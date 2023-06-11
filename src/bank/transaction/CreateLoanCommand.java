package bank.transaction;

import java.time.Period;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidInputException;
import bank.product.Loan;
import bank.product.account.AAccount;

public class CreateLoanCommand extends ATransactionCommand {

    private Customer owner;
    private AAccount account;
    private Bank bank;
    private Period period;
    private double amount;

    public CreateLoanCommand(Bank bank, Customer owner, AAccount account, Period period, double amount) {
        super("Create Loan", 
            new StringBuilder("Creating Loan of ")
            .append(amount)
            .append(" linked to Account ")
            .append(account.getId())
            .append(" to be reinbursed before ")
            .append(period.toString()).toString());
        this.account = account;
        this.bank = bank;
        this.owner = owner;
        this.period = period;
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        try {
            Loan loan = bank.createLoan(owner,account,period,amount);
            loan.log(this);
            return true;
        } catch (InvalidInputException e) {
            return false;
        }
    }

    @Override
    public double getValue() {
        return -amount;
    }
    
}