package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.product.account.AAccount;

import java.time.Period;

public class CalculateInterestCommand extends ATransactionCommand {

    private Customer owner;
    private AAccount account;
    private double amount;
    private Period period;
    private Bank bank;



    public CalculateInterestCommand(Bank bank, AAccount account, Period period, double amount, Customer owner ) {
        super(CalculateInterestCommand.class.getSimpleName(), "Interest");
        this.bank = bank;
        this.account = account;
        this.period = period;
        this.amount = amount;
        this.owner = owner;

    }

    @Override
    public void execute() {

        System.out.println("New Interest : " /*+ account.getInterest()*/ );
    }


}
