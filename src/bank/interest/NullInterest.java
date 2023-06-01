package bank.interest;

import bank.product.Product;

public class NullInterest implements State {

    Product product;

    public NullInterest(Product product) {
        this.product = product;
    }

    public double calculate() {
        return product.getBalance() * 0.0;
    }
    
}
