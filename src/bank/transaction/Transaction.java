package bank.transaction;

import java.time.LocalDate;

public abstract class Transaction {

    private String type;
    private LocalDate date = LocalDate.now();
    private String description;

    public Transaction(String type, String description) {
        this.type = type;
        this.description = description;
    }

    abstract public void execute();
    
}
