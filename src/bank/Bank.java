package bank;

import java.time.Period;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import bank.ibpa.IntBnkPmtAgy;
import bank.product.Credit;
import bank.product.Deposit;
import bank.product.Loan;
import bank.product.Product;
import bank.product.account.Account;
import bank.product.account.BaseAccount;
import bank.product.account.Debit;
import bank.transaction.Transaction;

public class Bank {
    // key is the ID of the bank assigned by the IBPA
    private Map<String, IntBnkPmtAgy> IBPAs = new HashMap<String, IntBnkPmtAgy>();
    private String name;
    private List<Transaction> history = new LinkedList<>();
    private Map<String, Customer> customers = new HashMap<String, Customer>();
    private Map<String, Account> accounts = new HashMap<String, Account>();

    public Bank(String name) {
        this.name = name;
    }

    public void addIBPA(String ID, IntBnkPmtAgy IBPA) {
        IBPAs.put(ID, IBPA);
    }

    public String getName() {
        return name;
    }

    public void registerAtIBPA(IntBnkPmtAgy IBPA) {
        assert !IBPAs.containsValue(IBPA) : "Already registered";
        IBPA.notify(this, "register");
    }

    /*
     * Here we suppose the ID is the national ID of the customer
     */
    public Customer registerCustomer(String ID, String name) {
        assert !customers.containsKey(ID) : "Customer already registered";
        Customer customer = new Customer(ID, name, this);
        customers.put(ID, customer);
        return customer;
    }

    public Product createAccount(Customer owner) {
        assert owner.getBank() == this : "Customer is not registered at this bank";
        String ID = generateAccountID();
        Account account = new BaseAccount(ID, owner);
        owner.addProduct(account);
        accounts.put(ID, account);
        return account;
    }
    
    public Product createCredit(Customer owner, double limit) {
        assert owner.getBank() == this : "Customer is not registered at this bank";
        Credit credit = new Credit(limit);
        owner.addProduct(credit);
        return credit;
    }

    public Product createLoan(Customer owner, Account account, Period period, double amount) {
        assert owner.getBank() == this : "Customer is not registered at this bank";
        Loan loan = new Loan(account, period, amount);
        owner.addProduct(loan);
        return loan;
    }

    public Product createDeposit(Customer owner, Account account, Period period, double amount) {
        assert owner.getBank() == this : "Customer is not registered at this bank";
        Deposit deposit = new Deposit(account, period, amount);
        owner.addProduct(deposit);
        return deposit;
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

}
