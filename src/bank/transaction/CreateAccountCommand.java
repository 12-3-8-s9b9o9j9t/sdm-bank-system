package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidCustomerException;
import bank.product.account.AAccount;

public class CreateAccountCommand extends ATransactionCommand {

    private Customer owner;
    private Bank bank;

    public CreateAccountCommand(Bank bank, Customer owner) {
        super("Create Account" , 
            new StringBuilder("Creating Account")
            .append(" for Customer ")
            .append(owner.getName())
            .append(" (")
            .append(owner.getId())
            .append(")").toString());
        this.owner = owner;
        this.bank = bank;
    }

    @Override
    public boolean execute() {
        try {
            AAccount account = bank.createAccount(owner);
            account.log(this);
            return true;
        } catch (InvalidCustomerException e) {
            return false;
        }
    }

    @Override
    public double getValue() {
        return 0;
    }
}