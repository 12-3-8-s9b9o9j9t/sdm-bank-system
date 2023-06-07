package bank.transaction;

import bank.exception.InvalidTransactionException;
import bank.product.ISuppliable;
import bank.product.Product;

public class SupplyProductCommand extends ATransactionCommand {

    private ISuppliable supplied;
    private double amount;


    public SupplyProductCommand(ISuppliable supplied, double amount) {
        super("Supply Product", "Supplying Product");
        this.supplied = supplied;
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        boolean success = true;
        Product product = (Product) supplied;
        try {
            supplied.supply(amount);
        }
        catch (InvalidTransactionException e) {
            setDescription(getDescription() + " : Failed");
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
