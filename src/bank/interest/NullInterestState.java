package bank.interest;

import java.time.LocalDate;

import bank.product.Product;

public class NullInterestState extends AInterestState {

    private Product product;

    public NullInterestState(Product product) {
        this.product = product;
    }

    public double calculate(LocalDate starDate, LocalDate endDate) {
        return product.getBalance() * 0.0;
    }
    
}
