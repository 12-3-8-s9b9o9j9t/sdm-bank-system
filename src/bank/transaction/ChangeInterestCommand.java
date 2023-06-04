package bank.transaction;

import bank.interest.AInterestState;
import bank.product.Product;

public class ChangeInterestCommand extends ATransactionCommand {

    private Product product;
    private AInterestState state;

    public ChangeInterestCommand(Product product, AInterestState state) {
        super("Set Interest", "Setting Interest");
        this.product = product;
        this.state = state;
    }

    @Override
    public void execute() {
        product.setInterest(state);
        product.log(this);
    }
    
}
