package bank.interest;

import bank.product.Product;

public class CustomInterest implements State {

    private Product product;
    private Interest interest;
    private double interest_rate;

    public CustomInterest(double _interest_rate, Product product, Interest interest) {
        this.product = product;
        this.interest = interest;
        interest_rate = _interest_rate;
    }

    public double calculate() {
        return product.getBalance() * interest_rate * interest.getDurationInYears();
    }
    
}
