package bank.interest;

import bank.product.Product;

public class HighInterest implements State {

    Product product;
    Interest interest;
    private double interest_rate = 0.02;

    public HighInterest(Product product, Interest interest) {
        this.product = product;
        this.interest = interest;
    }

    public double calculate() {
        return product.getBalance() * interest_rate * interest.getDurationInYears();
    }
    
}
