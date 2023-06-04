package bank.product.account;

import bank.interest.AInterestState;
import bank.product.Product;

public abstract class AAccount extends Product {

    public AAccount(String ID) {
        super(ID);
    }

    abstract public double getBalance();

    abstract public void setBalance(double balance);

    abstract public void pay(double amount);

    abstract public void receive(double amount);

    abstract public void transfert(AAccount accountIDSend, AAccount accountIDRecept, double amount);

    abstract public void withdraw(double amount);

}
