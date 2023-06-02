package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.product.account.AAccount;

public class Deposit extends Product {

    private AAccount account;
    private LocalDate targetDate;
    private double max;
    private double current = 0;

    public Deposit(AAccount account, Period period, double max) {
        this.targetDate = LocalDate.now().plus(period);
        this.account = account;
        this.max = max;
        setInterest(null /* TODO */);
    }

    public void supply(double amount) {
        // TODO Auto-generated method stub

    }

    public void close() {
        // TODO Auto-generated method stub

    }

}
