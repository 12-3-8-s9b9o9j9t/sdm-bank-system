package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidInputException;
import bank.product.Credit;

public class CreateCreditCommand extends ATransactionCommand {

    private Bank bank;
    private Customer owner;
    private double limit;

    public CreateCreditCommand(Bank bank, Customer owner, double limit) {
        super("Create Credit",
            new StringBuilder("Creating Credit for Customer ")
            .append(owner.getName())
            .append(" (")
            .append(owner.getId())
            .append(")")
            .append(" with limit of ")
            .append(limit).toString());
        this.bank = bank;
        this.owner = owner;
        this.limit = limit;
    }

    @Override
    public boolean execute() {
        try {
            Credit credit = bank.createCredit(owner, limit);
            credit.log(this);
            return true;
        } catch (InvalidInputException e) {
            return false;
        }
    }

    @Override
    public double getValue() {
        return 0;
    }
    
}
