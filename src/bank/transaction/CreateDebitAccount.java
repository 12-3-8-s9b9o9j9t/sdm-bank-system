package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.product.account.Account;

public class CreateDebitAccount extends Transaction{

    private Bank bank;
    private Customer owner;
    private Account account;

    public CreateDebitAccount(Bank bank, Customer owner, Account account) {
        super("createDebitAccount", "debit account");
        this.bank = bank;
        this.owner = owner;
        this.account = account;
    }


    @Override
    public void execute() {
        bank.createDebitAccount(owner,account);
    }


    
}
