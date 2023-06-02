package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.interest.AInterestState;
import bank.product.account.AAccount;

public class Loan extends Product {

    private AAccount account;
    private LocalDate targetDate;
    private double amount;

    public Loan(AAccount account, Period period, double amount) {
        this.targetDate = LocalDate.now().plus(period);
        this.account = account;
        this.amount = amount;
        setInterest(null /* TODO */);
    }

    public void borrow() {
        // TODO Auto-generated method stub

    }

    public void repay(double amount) {
        // TODO Auto-generated method stub

    }
    
}
