package bank.transaction;

import bank.Bank;
import bank.product.account.AAccount;

public class CloseAccountCommand {

    private AAccount account;
    private Bank bank;
    private String accountID;

    public CloseAccountCommand(Bank bank, AAccount account, String accountID) {
        this.bank = bank;
        this.account = account;
        this.accountID = accountID;
    }

    @Override
    public void execute() {
        account = null;
        log(bank, null);
        System.out.println("Account with ID : " + accountID + "is deleted" );

    }

}
