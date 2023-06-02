package bank.product.account;

public abstract class AAccountDecorator extends AAccount {
    private AAccount wrapee;

    public AAccountDecorator(AAccount wrapee) {
        this.wrapee = wrapee;
    }

    public String getID() {
        return wrapee.getID();
    }

    public int hashCode() {
        return wrapee.hashCode();
    }

    public boolean equals(Object obj) {
        return wrapee.equals(obj);
    }
    
}
