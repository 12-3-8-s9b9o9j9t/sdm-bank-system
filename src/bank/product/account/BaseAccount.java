package bank.product.account;

import java.time.LocalDate;

import bank.Customer;
import bank.interest.AInterestState;

public class BaseAccount extends AAccount {

    private Customer owner;
    private LocalDate openDate = LocalDate.now();
    private double balance = 0;

    public BaseAccount(String ID, Customer owner) {
        super(ID);
        this.owner = owner;
        setInterest(null /* TODO */);
    }

    @Override
    public void pay() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void receive() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void transfert() {
        // TODO Auto-generated method stub
        
    }

}
