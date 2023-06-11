package bank.transaction;

import bank.exception.InvalidTransactionException;
import bank.product.IChargeable;
import bank.product.AProduct;

public class ChargeProductCommand extends ATransactionCommand {

    private IChargeable charged;
    private double amount;

    public ChargeProductCommand(IChargeable charged, double amount) {
        super("Charge Product",
            new StringBuilder("Charging ")
            .append(amount)
            .append(" to product ")
            .append(((AProduct)charged).getId()).toString());
        this.charged = charged;
        this.amount = amount;
    }

    @Override
    public boolean execute() {
        boolean success = true;
        AProduct product = (AProduct) charged;
        try {
            charged.charge(amount);
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
        return -amount;
    }

}
