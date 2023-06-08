package bank.transaction;

import bank.interest.AInterestStrategy;
import bank.product.Product;

public class ChangeInterestCommand extends ATransactionCommand {

    private Product product;
    private AInterestStrategy state;

    public ChangeInterestCommand(Product product, AInterestStrategy state) {
        super("Set Interest",
            new StringBuilder("Changing Interest Mechanism for Product ")
            .append(product.getID())
            .append(" to ")
            .append(state.getClass().getSimpleName()).toString());
        this.product = product;
        this.state = state;
    }

    @Override
    public boolean execute() {
        product.setInterest(state);
        product.log(this);
        return true;
    }

    @Override
    public double getValue() {
        return 0;
    }
    
}
