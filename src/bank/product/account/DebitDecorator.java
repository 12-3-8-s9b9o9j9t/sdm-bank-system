package bank.product.account;

import bank.reporter.IVisitor;

public class DebitDecorator extends AAccountDecorator {
    
    private double overdraft;

    public DebitDecorator(AAccount wrapee) {
        super(wrapee);
    }

    public double getOverdraft() {
        return overdraft;
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

    @Override
    public void accept(IVisitor visitor) {
        visitor.visitDebitAccount(this);
    }
}
