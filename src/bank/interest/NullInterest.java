package bank.interest;

import java.time.LocalDate;

import bank.product.Product;

public class NullInterest extends Interest {

    private Product product;

    public NullInterest(Product product) {
        this.product = product;
    }

    public double calculate(LocalDate starDate, LocalDate endDate) {
        return product.getBalance() * 0.0;
    }
    
}
