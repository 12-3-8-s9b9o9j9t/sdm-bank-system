package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.product.Product;

public class CreateAccount extends Transaction {

    private Customer owner;
    private Bank bank;

    public CreateAccount(Bank bank, Customer owner) {
        super(CreateAccount.class.getSimpleName() , "base account");
        this.owner = owner;
        this.bank = bank;
    }

    @Override
    public void execute() {
        Product account = bank.createAccount(owner);
        log(bank, account);
    }
}