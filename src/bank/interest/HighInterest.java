package bank.interest;

import java.time.LocalDate;

import bank.product.Product;

public class HighInterest implements State {

    Product product;
    private double interest_rate = 0.02;

    public HighInterest(Product product) {
        this.product = product;
    }

    public double calculate(LocalDate starDate, LocalDate endDate) {
        return product.getBalance() * interest_rate * (endDate.getTime() - starDate.getTime()) / (1000 * 60 * 60 * 24 * 365);
    }
    
}
