package bank.product.account;

import bank.Bank;
import bank.Customer;
import bank.product.IChargeable;
import bank.product.ISuppliable;
import bank.product.AProduct;

public abstract class AAccount extends AProduct implements IChargeable, ISuppliable {

    public AAccount(String id, Bank bank) {
        super(id, bank);
    }

    protected Bank getBank() {
        return super.getBank();
    }

    abstract public double getBalance();

    abstract public Customer getOwner();
}
