package bank.transaction;

import java.time.LocalDate;

import bank.Bank;
import bank.product.Product;

public abstract class ATransactionCommand {

    private String type;
    private LocalDate date = LocalDate.now();
    private String description;

    public ATransactionCommand(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    abstract public boolean execute();
    
}
