package bank.transaction;

import bank.product.Product;

public class CalculateInterestCommand extends ATransactionCommand {

    private Product product;
    private double value = 0;

    public CalculateInterestCommand(Product product) {
        super("Calculate Interest", "Calculating Interest");
        this.product = product;
    }

    @Override
    public boolean execute() {
        value = product.calculateInterest();
        product.log(this);
        return true;
    }

    @Override
    public double getValue() {
        return value;
    }

}
