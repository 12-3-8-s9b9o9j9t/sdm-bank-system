package bank.transaction;

import bank.interest.AInterestStrategy;
import bank.product.Product;

public class ChangeInterestCommand extends ATransactionCommand {

    private Product product;
    private AInterestStrategy strategy;

    public ChangeInterestCommand(Product product, AInterestStrategy strategy) {
        super("Set Interest",
            new StringBuilder("Changing Interest Mechanism for Product ")
            .append(product.getId())
            .append(" to ")
            .append(strategy.getName()).toString());
        this.product = product;
        this.strategy = strategy;
    }

    @Override
    public boolean execute() {
        product.setInterest(strategy);
        product.log(this);
        return true;
    }

    @Override
    public double getValue() {
        return 0;
    }
    
}
