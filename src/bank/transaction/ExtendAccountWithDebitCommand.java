package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.exception.InvalidInputException;
import bank.product.account.AAccount;

public class ExtendAccountWithDebitCommand extends ATransactionCommand {

    private Bank bank;
    private Customer owner;
    private AAccount account;
    private double limit;

    public ExtendAccountWithDebitCommand(Bank bank, Customer owner, AAccount account, double limit) {
        super("Extend Account With Debit", 
            new StringBuilder("Extending Account ")
            .append(account.getId())
            .append(" with Debit capability of ")
            .append(limit).toString());
        this.bank = bank;
        this.owner = owner;
        this.account = account;
        this.limit = limit;
    }
    
    @Override
    public boolean execute() {
        try {
            AAccount debit = bank.extendAccountWithDebit(owner, account, limit);
            debit.log(this);
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
