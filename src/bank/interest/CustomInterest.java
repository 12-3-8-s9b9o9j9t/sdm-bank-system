package bank.interest;

import java.sql.Date;

import bank.product.Product;

public class CustomInterest implements State {

    private Product product;
    private double interest_rate;

    public CustomInterest(double _interest_rate, Product product, Interest interest) {
        this.product = product;
        interest_rate = _interest_rate;
    }

    public double calculate(Date starDate, Date endDate) {
        return product.getBalance() * interest_rate * (endDate.getTime() - starDate.getTime()) / (1000 * 60 * 60 * 24 * 365);
    }
    
}
