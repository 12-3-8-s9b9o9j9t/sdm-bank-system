package bank.product.account;

public class DebitDecorator extends AAccountDecorator {
    
    private double overdraft;

    public DebitDecorator(AAccount wrapee) {
        super(wrapee);
    }

    @Override
    public void pay() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void receive() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void transfert() {
        // TODO Auto-generated method stub
        
    }
}
