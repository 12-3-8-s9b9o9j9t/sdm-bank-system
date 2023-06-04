package bank.product.account;

import bank.product.Product;
import bank.reporter.IVisitor;

public abstract class AAccount extends Product {

    abstract public String getID();

    abstract public void pay();

    abstract public void receive();

    abstract public void transfert();

    public abstract void accept(IVisitor visitor);

}
