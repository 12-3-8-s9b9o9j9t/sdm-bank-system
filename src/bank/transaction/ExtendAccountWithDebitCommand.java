package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.product.account.AAccount;

public class ExtendAccountWithDebitCommand extends ATransactionCommand {

    private Bank bank;
    private Customer owner;
    private AAccount account;
    private double limit;

    public ExtendAccountWithDebitCommand(Bank bank, Customer owner, AAccount account, double limit) {
        super("Extend Account With Debit", "Extending Account With Debit");
        this.bank = bank;
        this.owner = owner;
        this.account = account;
        this.limit = limit;
    }
    
    @Override
    public void execute() {
        bank.extendAccountWithDebit(owner, account, limit);
        account.log(this);
    }
    
}
