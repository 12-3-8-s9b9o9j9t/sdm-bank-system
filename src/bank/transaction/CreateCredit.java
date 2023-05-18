package bank.transaction;

import bank.Bank;
import bank.Customer;

public class CreateCredit extends Transaction {

    private Bank bank;
    private Customer owner;
    private double limit;

    public CreateCredit(Bank bank, Customer owner, double limit) {
        super("createDebit", "debit account");
        this.bank = bank;
        this.owner = owner;
        this.limit = limit;
    }

    @Override
    public void execute() {
        bank.createCredit(owner, limit);
    }
    
        
    
}
