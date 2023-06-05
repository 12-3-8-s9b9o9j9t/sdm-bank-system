package bank.product.account;

import bank.Customer;

public abstract class AAccountDecorator extends AAccount {
    protected AAccount wrapee;

    public AAccountDecorator(AAccount wrapee) {
        super(wrapee.getID(), wrapee.getBank());
        this.wrapee = wrapee;
        this.setHistory(wrapee.getHistory());
    }

    @Override
    public String getID() {
        return wrapee.getID();
    }

    @Override
    public double getBalance() {
        return wrapee.getBalance();
    }

    @Override
    public Customer getOwner() {
        return wrapee.getOwner();
    }
    
}
