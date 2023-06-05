package bank.interest;

import bank.product.Product;

public class NullInterestStrategy extends AInterestStrategy {

    @Override
    public double calculate(Product product) {
        return 0;
    }
    
}
