package bank.interest;

import java.time.LocalDate;

import bank.product.Product;

public class NullInterest implements State {

    Product product;

    public NullInterest(Product product) {
        this.product = product;
    }

    public double calculate(Date starDate, Date endDate) {
        return product.getBalance() * 0.0;
    }
    
}
