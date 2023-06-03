package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.product.account.AAccount;

import java.time.Period;

public class WithdrawCommand {

    private Customer owner;
    private AAccount account;
    private double amount;
    private Period period;
    private Bank bank;

    public WithdrawCommand(Bank bank, AAccount account, Period period, double amount, Customer owner) {
        this.bank = bank;
        this.account = account;
        this.period = period;
        this.amount = amount;
        this.owner = owner;
    }

    @Override
    public void execute() {
        //Creation of the product & call for product log
        bank.makeWithdraw(owner, account, period, amount);

        //Log of the product
        log(bank, account);

        //Return message to consol to validate transaction
        System.out.println("Deposit of " + amount + " to account " + owner.getID() + " executed.");
    }


}
