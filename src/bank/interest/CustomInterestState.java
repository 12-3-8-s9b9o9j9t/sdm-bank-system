package bank.interest;

import java.time.LocalDate;

import bank.product.Product;

public class CustomInterestState extends AInterestState {

    private Product product;
    private double interest_rate;

    public CustomInterestState(double _interest_rate, Product product) {
        this.product = product;
        interest_rate = _interest_rate;
    }

    public double calculate(LocalDate starDate, LocalDate endDate) {
        return product.getBalance() * interest_rate * getDurationInYears(starDate, endDate);
    }
    
}
