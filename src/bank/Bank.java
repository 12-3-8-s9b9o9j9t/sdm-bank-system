package bank;

import java.time.Period;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import bank.exception.BankAlreadyRegisteredAtIBPAException;
import bank.exception.CustomerAlreadyExistsException;
import bank.exception.InvalidCustomerException;
import bank.ibpa.InterBankPaymentAgencyMediator;
import bank.interest.AInterestState;
import bank.product.Credit;
import bank.product.Deposit;
import bank.product.Loan;
import bank.product.Product;
import bank.product.account.AAccount;
import bank.product.account.BaseAccount;
import bank.product.account.DebitDecorator;
import bank.transaction.ATransactionCommand;
import bank.transaction.ChangeInterestCommand;

public class Bank {
    // key is the ID of the bank assigned by the IBPA
    private Map<String, InterBankPaymentAgencyMediator> IBPAs = new HashMap<>();
    private String name;
    private List<ATransactionCommand> history = new LinkedList<>();
    private Map<Integer, Customer> customers = new HashMap<>();

    public Bank(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addIBPA(String ID, InterBankPaymentAgencyMediator IBPA) {
        if (IBPAs.containsValue(IBPA)) {
            throw new BankAlreadyRegisteredAtIBPAException(name, IBPA.getName());
        }
        IBPAs.put(ID, IBPA);
    }

    public void registerAtIBPA(InterBankPaymentAgencyMediator IBPA) {
        if (IBPAs.containsValue(IBPA)) {
            throw new BankAlreadyRegisteredAtIBPAException(name, IBPA.getName());
        }
        IBPA.notify(this, "register");
    }

    /*
     * Here we suppose the ID is the national ID of the customer
     */
    public Customer registerCustomer(String ID, String name) {
        int hash = ID.hashCode();
        if (customers.containsKey(hash)) {
            throw new CustomerAlreadyExistsException(ID);
        }
        Customer customer = new Customer(hash, name, this);
        customers.put(hash, customer);
        return customer;
    }

    public Product createAccount(Customer owner) {
        checkCustomer(owner);
        String ID = generateProductID();
        AAccount account = new BaseAccount(ID, this, owner);
        owner.addProduct(account);
        return account;
    }
    
    public Product createCredit(Customer owner, double limit) {
        checkCustomer(owner);
        String ID = "CRD" + generateProductID();
        Credit credit = new Credit(ID, this, limit);
        owner.addProduct(credit);
        return credit;
    }

    public Product createLoan(Customer owner, AAccount account, Period period, double amount) {
        checkCustomer(owner);
        String ID = "LOA" + generateProductID();
        Loan loan = new Loan(ID, this, account, period, amount);
        owner.addProduct(loan);
        return loan;
    }

    public Product createDeposit(Customer owner, AAccount account, Period period, double amount) {
        checkCustomer(owner);
        String ID = "DEP" + generateProductID();
        Deposit deposit = new Deposit(ID, this, account, period, amount);
        owner.addProduct(deposit);
        return deposit;
    }

    public Product extendAccountWithDebit(Customer owner, AAccount account, double limit) {
        checkCustomer(owner);
        DebitDecorator debit = new DebitDecorator(account, limit);
        owner.addProduct(debit);
        return debit;
    }

    public void changeInterest(int customerID, String productID, AInterestState state) {
        Customer customer = customers.get(customerID);
        Product product = customer.getProduct(productID);
        new ChangeInterestCommand(product, state)
            .execute();
    }

    private void checkCustomer(Customer owner) {
        if (owner.getBank() != this) {
            throw new InvalidCustomerException(name);
        }
    }

    private String generateProductID() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .toUpperCase(Locale.ENGLISH);
    }

    public void log(ATransactionCommand transaction) {
        history.add(transaction);
    }

}
