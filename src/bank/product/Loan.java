package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.product.account.Account;

public class Loan extends Product {

    private Account account;
    private LocalDate targetDate;
    private double amount;

    public Loan(Account account, Period period, double amount) {
        this.targetDate = LocalDate.now().plus(period);
        this.account = account;
        this.amount = amount;
    }

    public void borrow() {
        // TODO Auto-generated method stub

    }

    public void repay(double amount) {
        // TODO Auto-generated method stub

    }
    
}
