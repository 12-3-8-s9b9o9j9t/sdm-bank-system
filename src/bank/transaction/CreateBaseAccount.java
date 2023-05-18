package bank.transaction;

import bank.Bank;
import bank.Customer;

class CreateBaseAccount extends Transaction {

    private Customer owner;
    private Bank bank;

    CreateBaseAccount(Bank bank, Customer owner) {
        super("createBaseAccount", "base account");
        this.owner = owner;
        this.bank = bank;
    }

    @Override
    public void execute() {
        bank.openAccount(owner);
    }
}