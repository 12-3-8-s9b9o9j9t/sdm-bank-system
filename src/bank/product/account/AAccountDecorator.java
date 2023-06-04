package bank.product.account;

public abstract class AAccountDecorator extends AAccount {
    protected AAccount wrapee;

    public AAccountDecorator(AAccount wrapee) {
        super(wrapee.getID());
        this.wrapee = wrapee;
    }

    public String getID() {
        return wrapee.getID();
    }
    
}
