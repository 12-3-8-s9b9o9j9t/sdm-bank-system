package bank.transaction;

import bank.exception.InvalidTransactionException;
import bank.product.IChargeable;
import bank.product.Product;

public class ChargeProductCommand extends ATransactionCommand {

    private IChargeable charged;
    private double amount;

    public ChargeProductCommand(IChargeable charged, double amount) {
        super("Cahrge Product", "Chargeing Product");
        this.charged = charged;
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        boolean success = true;
        Product product = (Product) charged;
        try {
            charged.charge(amount);
        }
        catch (InvalidTransactionException e) {
            setDescription(getDescription() + " : Failed");
            success = false;
        }
        product.log(this);
        return success;
    }


}
