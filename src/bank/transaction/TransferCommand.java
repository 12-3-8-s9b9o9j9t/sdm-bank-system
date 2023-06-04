package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.product.account.AAccount;

import java.time.Period;

public class TransferCommand extends ATransactionCommand {

    private Customer owner;
    private AAccount account;
    private AAccount accountRecept;
    private double amount;
    private Period period;
    private Bank bank;
    // private Card card;


    public TransferCommand(Bank bank, AAccount account, AAccount accountRecept, Period period, double amount, Customer owner) {
        super(TransferCommand.class.getSimpleName(), "Transfer");
        this.bank = bank;
        this.account = account;
        this.accountRecept = accountRecept;
        this.period = period;
        this.amount = amount;
        this.owner = owner;
    }

    @Override
    public void execute() {
        //Creation of the product & call for product log
        bank.makeTransfert(owner, account, accountRecept, period, amount);

        //Log of the product
        log(bank, account);
        //Return message to consol to validate transaction
        System.out.println("Transfert of " + amount + " from account " + owner.getID() + " to account" + accountRecept.getID() );
    }

}
