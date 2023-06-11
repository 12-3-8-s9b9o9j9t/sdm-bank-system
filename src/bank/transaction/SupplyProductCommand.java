package bank.transaction;

import bank.exception.InvalidTransactionException;
import bank.product.ISuppliable;
import bank.product.AProduct;

public class SupplyProductCommand extends ATransactionCommand {

    private ISuppliable supplied;
    private double amount;


    public SupplyProductCommand(ISuppliable supplied, double amount) {
        super("Supply Product", 
            new StringBuilder("Supplying ")
            .append(amount)
            .append(" to product ")
            .append(((AProduct)supplied).getId()).toString());
        this.supplied = supplied;
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        boolean success = true;
        AProduct product = (AProduct) supplied;
        try {
            supplied.supply(amount);
        }
        catch (InvalidTransactionException e) {
            setDescription(getDescription() + ": Failed");
            success = false;
        }
        product.log(this);
        return success;
    }

    @Override
    public double getValue() {
        return amount;
    }

}
