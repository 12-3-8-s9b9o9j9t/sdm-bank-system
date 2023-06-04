package bank.product.account;

import bank.interest.AInterestState;
import bank.product.Product;

public abstract class AAccount extends Product {

    public AAccount(String ID) {
        super(ID);
    }

    abstract public void pay();

    abstract public void receive();

    abstract public void transfert();

}
