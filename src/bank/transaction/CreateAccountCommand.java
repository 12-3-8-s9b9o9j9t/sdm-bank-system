package bank.transaction;

import bank.Bank;
import bank.Customer;
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
    public void execute() {
        Product account = bank.createAccount(owner);
        account.log(this);
    }
}