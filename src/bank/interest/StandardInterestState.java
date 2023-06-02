package bank.interest;

import java.time.LocalDate;

import bank.product.Product;

public class StandardInterestState extends AInterestState {

    private Product product;
    private double interest_rate = 0.01;

    public StandardInterestState(Product product) {
        this.product = product;
    }

    public double calculate(LocalDate starDate, LocalDate endDate) {
        return product.getBalance() * interest_rate * getDurationInYears(starDate, endDate);
    }
    
}
