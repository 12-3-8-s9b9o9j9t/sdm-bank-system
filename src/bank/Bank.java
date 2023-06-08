package bank;

import java.time.Period;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.List;
import java.util.UUID;

import bank.exception.BankAlreadyRegisteredAtIBPAException;
import bank.exception.CustomerAlreadyExistsException;
import bank.exception.InvalidCustomerException;
import bank.ibpa.InterBankPaymentAgencyMediator;
import bank.interest.AInterestStrategy;
import bank.product.Credit;
import bank.product.Deposit;
import bank.product.Loan;
import bank.product.Product;
import bank.product.account.AAccount;
import bank.product.account.BaseAccount;
import bank.product.account.DebitDecorator;
import bank.transaction.ATransactionCommand;
import bank.transaction.CalculateInterestCommand;
import bank.transaction.ChangeInterestCommand;
import bank.transaction.transfer.TransferCommand;

public class Bank {
    // key is the ID of the bank assigned by the IBPA
    private Map<String, InterBankPaymentAgencyMediator> IBPAs = new HashMap<>();
    private String name;
    private List<ATransactionCommand> history = new LinkedList<>();
    private Map<Integer, Customer> customers = new HashMap<>();
    private Map<String, AAccount> accounts = new HashMap<>();
    
    private List<TransferCommand> insidePendingTransfers = new LinkedList<>();
    private Map<String, List<TransferCommand>> outsidePendingTransfers = new HashMap<>();

    public Bank(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addIBPA(String ID, InterBankPaymentAgencyMediator IBPA) {
        IBPAs.put(ID, IBPA);
        outsidePendingTransfers.put(IBPA.getName(), new LinkedList<>());
    }

    public void registerAtIBPA(InterBankPaymentAgencyMediator IBPA) throws Exception {
        if (IBPAs.containsValue(IBPA)) {
            throw new BankAlreadyRegisteredAtIBPAException(name, IBPA.getName());
        }
        IBPA.notify(this, "register");
    }

    /*
     * Here we suppose the ID is the national ID of the customer
     */
    public Customer registerCustomer(String ID, String name, String password) throws CustomerAlreadyExistsException {
        int hash = ID.hashCode();
        if (customers.containsKey(hash)) {
            throw new CustomerAlreadyExistsException(ID);
        }
        Customer customer = new Customer(hash, name, password, this);
        customers.put(hash, customer);
        return customer;
    }

    public Product createAccount(Customer owner) throws InvalidCustomerException {
        checkCustomer(owner);
        String ID = generateProductID();
        AAccount account = new BaseAccount(ID, this, owner);
        owner.addProduct(account);
        accounts.put(ID, account);
        return account;
    }
    
    public Product createCredit(Customer owner, double limit) throws InvalidCustomerException {
        checkCustomer(owner);
        String ID = "CRD" + generateProductID();
        Credit credit = new Credit(ID, this, limit);
        owner.addProduct(credit);
        return credit;
    }

    public Product createLoan(Customer owner, AAccount account, Period period, double amount) throws InvalidCustomerException {
        checkCustomer(owner);
        String ID = "LOA" + generateProductID();
        Loan loan = new Loan(ID, this, account, period, amount);
        owner.addProduct(loan);
        return loan;
    }

    public Product createDeposit(Customer owner, AAccount account, Period period, double amount) throws InvalidCustomerException {
        checkCustomer(owner);
        String ID = "DEP" + generateProductID();
        Deposit deposit = new Deposit(ID, this, account, period, amount);
        owner.addProduct(deposit);
        return deposit;
    }

    public Product extendAccountWithDebit(Customer owner, AAccount account, double limit) throws InvalidCustomerException {
        checkCustomer(owner);
        DebitDecorator debit = new DebitDecorator(account, limit);
        owner.addProduct(debit);
        accounts.put(debit.getID(), debit);
        return debit;
    }

    public boolean changeInterest(int customerID, String productID, AInterestStrategy state) {
        Customer customer = customers.get(customerID);
        Product product = customer.getProduct(productID);
        return new ChangeInterestCommand(product, state)
            .execute();
    }

    public void calculateInterest() {
        for (Customer customer: customers.values()) {
            for (Product product: customer.getProducts().values()) {
                new CalculateInterestCommand(product)
                    .execute();
            }
        }
    }

    public void addPendingTransfer(TransferCommand transfer) {
        String bankID = transfer.getReceivingBankID();
        if (bankID != null && !IBPAs.containsKey(bankID)) {
            outsidePendingTransfers.get(transfer.getIBPAName()).add(transfer);
        } else {
            insidePendingTransfers.add(transfer);
        }
    }

    public List<TransferCommand> getPendingTransfers(String IBPAName) {
        return outsidePendingTransfers.get(IBPAName);
    }

    public void executeTransfers() {
        for (TransferCommand transfer: insidePendingTransfers) {
            transfer.setReceivingAccount(accounts.get(transfer.getReceivingAccountID()));
            transfer.execute();
        }
        insidePendingTransfers.clear();
    }

    private void checkCustomer(Customer owner) throws InvalidCustomerException {
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
