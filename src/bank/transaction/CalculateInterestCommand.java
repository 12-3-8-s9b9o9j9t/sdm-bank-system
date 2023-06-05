package bank.transaction;

import bank.product.Product;

public class CalculateInterestCommand extends ATransactionCommand {

    private Product product;

    public CalculateInterestCommand(Product product) {
        super("Calculate Interest", "Calculating Interest");
        this.product = product;
    }

    @Override
    public boolean execute() {
        product.calculateInterest();
        product.log(this);
        return true;
    }


}
