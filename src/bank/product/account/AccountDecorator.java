package bank.product.account;

public abstract class AccountDecorator extends Account {
    private Account wrapee;

    public AccountDecorator(Account wrapee) {
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
