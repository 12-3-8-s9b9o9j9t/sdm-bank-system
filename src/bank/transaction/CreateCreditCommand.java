package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidCustomerException;
import bank.product.Product;

public class CreateCreditCommand extends ATransactionCommand {

    private Bank bank;
    private Customer owner;
    private double limit;

    public CreateCreditCommand(Bank bank, Customer owner, double limit) {
        super("Create Credit", "Creating Credit");
        this.bank = bank;
        this.owner = owner;
        this.limit = limit;
    }

    @Override
    public boolean execute() {
        try {
            Product credit = bank.createCredit(owner, limit);
            credit.log(this);
            return true;
        } catch (InvalidCustomerException e) {
            return false;
        }
    }
    
}
