package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidCustomerException;
import bank.product.Product;

public class CreateAccountCommand extends ATransactionCommand {

    private Customer owner;
    private Bank bank;

    public CreateAccountCommand(Bank bank, Customer owner) {
        super("Create Account" , "Creating Account");
        this.owner = owner;
        this.bank = bank;
    }

    @Override
    public boolean execute() {
        try {
            Product account = bank.createAccount(owner);
            account.log(this);
            return true;
        } catch (InvalidCustomerException e) {
            return false;
        }
    }
}