package bank.transaction;

import bank.product.AProduct;

public class CalculateInterestCommand extends ATransactionCommand {

    private AProduct product;
    private double value = 0;

    public CalculateInterestCommand(AProduct product) {
        super("Calculate Interest",
            new StringBuilder("Calculating Interest for Product ")
            .append(product.getId()).toString());
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
