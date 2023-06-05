package bank;

import bank.reporter.IVisitor;

public interface IElement {
    
    public String accept(IVisitor visitor);
        
}
