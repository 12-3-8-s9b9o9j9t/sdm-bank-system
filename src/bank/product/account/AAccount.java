package bank.product.account;

import bank.Bank;
import bank.product.IChargeable;
import bank.product.ISuppliable;
import bank.product.Product;

public abstract class AAccount extends Product implements IChargeable, ISuppliable {

    public AAccount(String ID, Bank bank) {
        super(ID, bank);
    }

    protected Bank getBank() {
        return super.getBank();
    }

    abstract public double getBalance();

}
