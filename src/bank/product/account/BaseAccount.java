package bank.product.account;

import java.time.LocalDate;

import bank.Customer;

public class BaseAccount extends Account {

    private String ID; // assigned by the bank
    private Customer owner;
    private LocalDate openDate = LocalDate.now();
    private double balance = 0;

    public BaseAccount(String ID, Customer owner) {
        this.ID = ID;
        this.owner = owner;
    }

    public String getID() {
        return ID;
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


    //The Account is defined by its ID
    //override the hash method to use ID
    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    //override the equals method to use ID
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            Account account = (Account) obj;
            return ID.equals(account.getID());
        }
        return false;
    }
}
