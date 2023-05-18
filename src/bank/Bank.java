package bank;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import bank.ibpa.BankMediator;
import bank.ibpa.InterbankPayAgy;
import bank.product.account.Account;
import bank.product.account.BaseAccount;
import bank.transaction.Transaction;

public class Bank {
    private String ID; // assigned by the IBPA
    private String name;
    private BankMediator IBPA;
    private List<Transaction> history = new LinkedList<>();
    private Set<Customer> customers = new HashSet<Customer>();
    private Map<String, Account> accounts = new HashMap<String, Account>();

    public Bank(String name) {
        this.name = name;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setIBPA(BankMediator IBPA) {
        this.IBPA = IBPA;
    }

    public String getName() {
        return name;
    }

    public boolean registerAtIBPA(InterbankPayAgy IBPA) {
        IBPA.notify(this, "register");
        return this.ID != null && this.IBPA == IBPA;
    }

    /*
     * Here we suppose the ID is the national ID of the customer
     */
    public boolean registerCustomer(String ID, String name) {
        Customer customer = new Customer(ID, name, this);
        return customers.add(customer);
    }

    public boolean openAccount(Customer owner) {
        String ID = generateAccountID();
        Account account = new BaseAccount(ID, owner);
        owner.addProduct(account);
        accounts.put(ID, account);
        return true;
    }

    private String generateAccountID() {
        return UUID.randomUUID()
            .toString()
            .replaceAll("-", "")
            .toUpperCase(Locale.ENGLISH);
    }

    public void log(Transaction transaction) {
        history.add(transaction);
    }

    //The Bank is defined by its BIC
    //override the hash method to use BIC
    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    //override the equals method to use BIC
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Bank) {
            Bank bank = (Bank) obj;
            return ID.equals(bank.ID);
        }
        return false;
    }

}