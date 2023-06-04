package bank.product.account;

import java.time.LocalDate;

import bank.Customer;
import bank.interest.AInterestState;

public class BaseAccount extends AAccount {

    private Customer owner;
    private LocalDate openDate = LocalDate.now();
    private double actualBalance;
    private double newCurrentBalance;


    private double balance = 0;

    public BaseAccount(String ID, Customer owner) {
        super(ID);
        this.owner = owner;
        setInterest(null /* TODO */);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public void pay(double amount ) {
        // TODO Auto-generated method stub
        this.balance = this.balance - amount;
        System.out.println("New balance : " + balance);


    }

    @Override
    public void receive(double amount) {
        // TODO Auto-generated method stub
        this.balance = this.balance + amount;
        System.out.println("You receive Money. New balance : " + balance);

    }

    @Override
    public void transfert(AAccount accountIDSend, AAccount accountIDRecept, double amount) {
        // TODO Auto-generated method stub
        //New solde for sending account
        actualBalance = accountIDSend.getBalance();
        newCurrentBalance = actualBalance - amount;
        accountIDSend.setBalance(newCurrentBalance);

        //Reset of Actual
        actualBalance = 0;
        newCurrentBalance = 0;


        //New solde for receiving Account
        actualBalance = accountIDRecept.getBalance();
        newCurrentBalance = actualBalance + amount;
        accountIDRecept.setBalance(newCurrentBalance);

        //Reset of Actual
        actualBalance = 0;
        newCurrentBalance = 0;

        System.out.println("Transfert Done. New balance : " + accountIDSend.getBalance());


    }

    @Override
    public void withdraw(double amount) {
        this.balance = this.balance - amount;
        System.out.println("Withdraw Done. New balance : " + balance);

    }

}
