package bank;

import bank.reporter.IVisitor;

public interface IElement {
    
    public void accept(IVisitor visitor);
        
}
