package bank;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import bank.ibpa.BankMediator;
import bank.ibpa.InterbankPayAgy;
import bank.transaction.Transaction;

public class Bank {
    private String BIC;
    private BankMediator IBPA;
    private List<Transaction> history = new LinkedList<>();
    private Set<Customer> customers = new HashSet<Customer>();

    public Bank(String BIC, InterbankPayAgy IBPA) {
        this.BIC = BIC;
        this.IBPA = IBPA;
        this.IBPA.notify(this, "register");
    }

    public boolean registerCustomer(String ID, String name) {
        Customer customer = new Customer(ID, name, this);
        return customers.add(customer);
    }

    public void log(Transaction transaction) {
        history.add(transaction);
    }


    //The Bank is defined by its BIC
    //override the hash method to use BIC
    @Override
    public int hashCode() {
        return BIC.hashCode();
    }

    //override the equals method to use BIC
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bank) {
            Bank bank = (Bank) obj;
            return BIC.equals(bank.BIC);
        }
        return false;
    }
}
