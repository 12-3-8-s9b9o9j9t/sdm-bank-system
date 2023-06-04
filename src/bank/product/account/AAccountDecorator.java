package bank.product.account;

public abstract class AAccountDecorator extends AAccount {
    protected AAccount wrapee;

    public AAccountDecorator(AAccount wrapee) {
        super(wrapee.getID(), wrapee.getBank());
        this.wrapee = wrapee;
        this.setHistory(wrapee.getHistory());
    }

    public String getID() {
        return wrapee.getID();
    }
    
}
