package bank.interest;

import java.sql.Date;

import bank.product.Product;

public class HighInterest implements State {

    Product product;
    private double interest_rate = 0.02;

    public HighInterest(Product product, Interest interest) {
        this.product = product;
    }

    public double calculate(Date starDate, Date endDate) {
        return product.getBalance() * interest_rate * (endDate.getTime() - starDate.getTime()) / (1000 * 60 * 60 * 24 * 365);
    }
    
}
