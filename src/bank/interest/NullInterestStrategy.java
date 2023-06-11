package bank.interest;

import bank.product.Product;

public class NullInterestStrategy extends AInterestStrategy {

    public NullInterestStrategy() {
        super("Null Interest");
    }

    @Override
    public double calculate(Product product) {
        return 0;
    }
    
}
