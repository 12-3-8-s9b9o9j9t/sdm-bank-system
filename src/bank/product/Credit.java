package bank.product;

import bank.reporter.IVisitor;

public class Credit extends Product {
    
    private double limit;
    private double amount;

    public Credit(double limit) {
        this.limit = limit;
        setInterest(null /* TODO */);
    }

    public double getLimit() {
        return limit;
    }

    public double getAmount() {
        return amount;
    }

    public void borrow(double amount) {
        // TODO Auto-generated method stub

    }

    public void repay(double amount) {
        // TODO Auto-generated method stub

    }

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitCredit(this);
    }
}
