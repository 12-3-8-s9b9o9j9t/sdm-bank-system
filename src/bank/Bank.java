package bank;

import java.time.Period;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import bank.ibpa.InterBankPaymentAgencyMediator;
import bank.product.Credit;
import bank.product.Deposit;
import bank.product.Loan;
import bank.product.Product;
import bank.product.account.AAccount;
import bank.product.account.BaseAccount;
import bank.product.account.DebitDecorator;
import bank.transaction.ATransactionCommand;

public class Bank {
    // key is the ID of the bank assigned by the IBPA
    private Map<String, InterBankPaymentAgencyMediator> IBPAs = new HashMap<>();
    private String name;
    private List<ATransactionCommand> history = new LinkedList<>();
    private Map<String, Customer> customers = new HashMap<>();
    private Map<String, AAccount> accounts = new HashMap<>();

    public Bank(String name) {
        this.name = name;
    }

    public void addIBPA(String ID, InterBankPaymentAgencyMediator IBPA) {
        IBPAs.put(ID, IBPA);
    }

    public String getName() {
        return name;
    }

    public void registerAtIBPA(InterBankPaymentAgencyMediator IBPA) {
        if (IBPAs.containsValue(IBPA)) {
            throw new RuntimeException("Bank already registered");
        }
        IBPA.notify(this, "register");
    }

    /*
     * Here we suppose the ID is the national ID of the customer
     */
    public Customer registerCustomer(String ID, String name) {
        if (customers.containsKey(ID)) {
            throw new RuntimeException("Customer already registered");
        }
        Customer customer = new Customer(ID, name, this);
        customers.put(ID, customer);
        return customer;
    }

    public Product createAccount(Customer owner) {
        if (owner.getBank() != this) {
            throw new RuntimeException("Customer not registered");
        }
        String ID = generateAccountID();
        AAccount account = new BaseAccount(ID, owner);
        owner.addProduct(account);
        accounts.put(ID, account);
        return account;
    }
    
    public Product createCredit(Customer owner, double limit) {
        if (owner.getBank() != this) {
            throw new RuntimeException("Customer not registered");
        }
        Credit credit = new Credit(limit);
        owner.addProduct(credit);
        return credit;
    }

    public Product createLoan(Customer owner, AAccount account, Period period, double amount) {
        if (owner.getBank() != this) {
            throw new RuntimeException("Customer not registered");
        }
        Loan loan = new Loan(account, period, amount);
        owner.addProduct(loan);
        return loan;
    }

    public Product createDeposit(Customer owner, AAccount account, Period period, double amount) {
        if (owner.getBank() != this) {
            throw new RuntimeException("Customer not registered");
        }
        Deposit deposit = new Deposit(account, period, amount);
        owner.addProduct(deposit);
        return deposit;
    }
    // Make Withdraw
    public void makeWithdraw(Customer owner, AAccount account, Period period, double amount) {
        if (owner.getBank() != this) {
            throw new RuntimeException("Customer not registered");
        }

        //Withdrawn on the account
        account.withdraw(amount);
    }

    // Make Payment
    public void makePayment(Customer owner, AAccount account, Period period, double amount, Card card) {
        if (owner.getBank() != this) {
            throw new RuntimeException("Customer not registered");
        }
        //Make Payment
        account.pay(amount);
    }

    // Make Transfert
    public void makeTransfert(Customer owner, AAccount account, AAccount accountrecept, Period period, double amount) {
        if (owner.getBank() != this) {
            throw new RuntimeException("Customer not registered");
        }
        //Make transfert
        account.transfert(account, accountrecept, amount);

    }

    private String generateAccountID() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .toUpperCase(Locale.ENGLISH);
    }

    public void log(ATransactionCommand transaction) {
        history.add(transaction);
    }

}
