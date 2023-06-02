package bank.interest;

import java.time.LocalDate;

import bank.product.Product;

public class LowInterest extends Interest {

    private Product product;
    private double interest_rate = 0.005;

    public LowInterest(Product product) {
        this.product = product;
    }

    public double calculate(LocalDate starDate, LocalDate endDate) {
        return product.getBalance() * interest_rate * getDurationInYears(starDate, endDate);
    }
    
}
