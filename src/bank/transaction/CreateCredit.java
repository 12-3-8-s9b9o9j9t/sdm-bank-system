package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.product.Product;

public class CreateCredit extends Transaction {

    private Bank bank;
    private Customer owner;
    private double limit;

    public CreateCredit(Bank bank, Customer owner, double limit) {
        super(CreateCredit.class.getSimpleName(), "debit account");
        this.bank = bank;
        this.owner = owner;
        this.limit = limit;
    }

    @Override
    public void execute() {
        Product credit = bank.createCredit(owner, limit);
        log(bank, credit);
    }
    
}
