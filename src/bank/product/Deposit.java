package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.product.account.AAccount;
import bank.reporter.IVisitor;

public class Deposit extends Product {

    private AAccount account;
    private LocalDate targetDate;
    private double limit;
    private double current = 0;

    public Deposit(AAccount account, Period period, double limit) {
        this.targetDate = LocalDate.now().plus(period);
        this.account = account;
        this.limit = limit;
        setInterest(null /* TODO */);
    }

    public AAccount getAccount() {
        return account;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public double getAmount() {
        return current;
    }

    public double getMax() {
        return limit;
    }

    public void supply(double amount) {
        // TODO Auto-generated method stub

    }

    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitDeposit(this);
    }
}
