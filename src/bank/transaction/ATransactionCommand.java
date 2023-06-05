package bank.transaction;

import java.time.LocalDate;

import bank.Bank;
import bank.IElement;
import bank.product.Product;
import bank.reporter.IVisitor;

public abstract class ATransactionCommand implements ICommand, IElement {

    private String type;
    private LocalDate date = LocalDate.now();
    private String description;

    public ATransactionCommand(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    abstract public boolean execute();
    
    public String accept(IVisitor visitor) {
        return visitor.visitTransaction(this);
    }
}
