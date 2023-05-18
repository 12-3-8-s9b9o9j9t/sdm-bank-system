package bank.product.account;

public class Debit extends AccountDecorator {
    
    private double overdraft;

    public Debit(Account wrapee) {
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
