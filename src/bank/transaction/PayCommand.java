package bank.transaction;

import bank.Bank;
import bank.Customer;
import bank.product.account.AAccount;

import java.awt.color.ProfileDataException;
import java.time.Period;

public class PayCommand extends ATransactionCommand {

    private Customer owner;
    private AAccount account;
    private double amount;
    private Period period;
    private Bank bank;
    // private Card card;


    public PayCommand(Bank bank, AAccount account, Period period, double amount, Customer owner/*, Card card*/) {
        super(PayCommand.class.getSimpleName(), "Pay");
        this.bank = bank;
        this.account = account;
        this.period = period;
        this.amount = amount;
        this.owner = owner;
        //this.card = card;
    }

    @Override
    public void execute() {
        //Creation of the product & call for product log
        bank.makePayment(owner, account, period, amount/* , card*/);

        //Log of the product
        log(bank, account);
        //Return message to consol to validate transaction

        System.out.println("Payment of " + amount + " to account " + owner.getID() + " executed with the card" /*+ card.getID*/ );
    }



}
