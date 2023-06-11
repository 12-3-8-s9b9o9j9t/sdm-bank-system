package bank.interest;

import bank.product.AProduct;

public class NullInterestStrategy extends AInterestStrategy {

    public NullInterestStrategy() {
        super("Null Interest");
    }

    @Override
    public double calculate(AProduct product) {
        return 0;
    }
    
}
