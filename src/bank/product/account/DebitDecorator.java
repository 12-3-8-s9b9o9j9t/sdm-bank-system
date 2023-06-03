package bank.product.account;

public class DebitDecorator extends AAccountDecorator {
    
    private double overdraft;

    public DebitDecorator(AAccount wrapee) {
        super(wrapee);
    }

    @Override
    public void pay(double amount) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void receive(double amount) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void transfert(AAccount accountIDSend, AAccount accountIDRecept, double amount) {
        // TODO Auto-generated method stub
        
    }
}
