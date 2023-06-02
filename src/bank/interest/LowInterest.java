package bank.interest;

import java.time.LocalDate;

import bank.product.Product;

public class LowInterest implements State {

    Product product;
    private double interest_rate = 0.005;

    public LowInterest(Product product) {
        this.product = product;
    }

    public double calculate(Date starDate, Date endDate) {
        return product.getBalance() * interest_rate * (endDate.getTime() - starDate.getTime()) / (1000 * 60 * 60 * 24 * 365);
    }
    
}
