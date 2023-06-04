package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.Bank;
import bank.interest.AInterestState;
import bank.product.account.AAccount;

public class Loan extends Product {

    private AAccount account;
    private LocalDate targetDate;
    private double amount;

    public Loan(String ID, Bank bank, AAccount account, Period period, double amount) {
        super(ID, bank);
        this.targetDate = getCreationDate().plus(period);
        this.account = account;
        this.amount = amount;
        this.account.supply(amount);
    }

    public void repay(double amount) {
        this.amount -= amount;
    }
    
}
