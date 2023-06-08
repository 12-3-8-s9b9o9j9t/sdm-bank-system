package bank.transaction;

import bank.product.Product;

public class CalculateInterestCommand extends ATransactionCommand {

    private Product product;
    private double value = 0;

    public CalculateInterestCommand(Product product) {
        super("Calculate Interest",
            new StringBuilder("Calculating Interest for Product ")
            .append(product.getID()).toString());
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
