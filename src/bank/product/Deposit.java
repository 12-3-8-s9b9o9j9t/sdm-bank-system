package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.product.account.Account;

public class Deposit extends Product {

    private Account account;
    private LocalDate targetDate;
    private double max;
    private double current = 0;

    Deposit(Account account, Period period, double max) {
        this.targetDate = LocalDate.now().plus(period);
        this.account = account;
        this.max = max;
    }

    public void supply(double amount) {
        // TODO Auto-generated method stub

    }

    public void close() {
        // TODO Auto-generated method stub

    }

}
