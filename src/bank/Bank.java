package bank;

import java.time.Period;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import bank.exception.BankAlreadyRegisteredAtIbpaException;
import bank.exception.CustomerAlreadyExistsException;
import bank.exception.InvalidAmountException;
import bank.exception.InvalidBankException;
import bank.exception.InvalidCustomerException;
import bank.exception.InvalidInputException;
import bank.exception.InvalidPeriodException;
import bank.exception.InvalidProductException;
import bank.ibpa.InterBankPaymentAgencyMediator;
import bank.interest.AInterestStrategy;
import bank.product.Credit;
import bank.product.Deposit;
import bank.product.Loan;
import bank.product.AProduct;
import bank.product.account.AAccount;
import bank.product.account.BaseAccount;
import bank.product.account.DebitDecorator;
import bank.transaction.ATransactionCommand;
import bank.transaction.CalculateInterestCommand;
import bank.transaction.ChangeInterestCommand;
import bank.transaction.transfer.TransferCommand;

public class Bank {
    // key is the id of the bank assigned by the ibpa
    private Map<String, InterBankPaymentAgencyMediator> ibpas = new HashMap<>();
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

    public List<TransferCommand> getPendingTransfers(String IbpaName) {
        return outsidePendingTransfers.get(IbpaName);
    }

    public void clearPendingTransfers(String IbpaName) {
        outsidePendingTransfers.get(IbpaName).clear();
    }

    public void log(ATransactionCommand transaction) {
        history.add(transaction);
    }

    public void addIbpa(String id, InterBankPaymentAgencyMediator ibpa) {
        ibpas.put(id, ibpa);
        outsidePendingTransfers.put(ibpa.getName(), new LinkedList<>());
    }

    public void addPendingTransfer(TransferCommand transfer) {
        String bankID = transfer.getReceivingBankID();
        if (bankID != null && !ibpas.containsKey(bankID)) {
            outsidePendingTransfers.get(transfer.getIbpaName()).add(transfer);
        } else {
            insidePendingTransfers.add(transfer);
        }
    }

    public void registerAtIbpa(InterBankPaymentAgencyMediator ibpa) throws BankAlreadyRegisteredAtIbpaException {
        if (ibpas.containsValue(ibpa)) {
            throw new BankAlreadyRegisteredAtIbpaException(name, ibpa.getName());
        }
        try {
            ibpa.notify(this, "register");
        } catch (InvalidBankException e) {
            // This should never happen
            throw new RuntimeException("Unexpected InvalidBankException occurred", e);
        }
    }

    /*
     * Here we suppose the id is the national id of the customer
     */
    public Customer registerCustomer(String id, String name, String password) throws CustomerAlreadyExistsException {
        int hash = id.hashCode();
        if (customers.containsKey(hash)) {
            throw new CustomerAlreadyExistsException(id);
        }
        Customer customer = new Customer(hash, name, password, this);
        customers.put(hash, customer);
        return customer;
    }

    public AAccount createAccount(Customer owner) throws InvalidCustomerException {
        checkCustomer(owner);
        String id = generateProductID();
        AAccount account = new BaseAccount(id, this, owner);
        owner.addProduct(account);
        accounts.put(id, account);
        return account;
    }
    
    public Credit createCredit(Customer owner, double limit) throws InvalidInputException {
        checkCustomer(owner);
        checkAmount(limit);
        String id = "CRD" + generateProductID();
        Credit credit = new Credit(id, this, limit);
        owner.addProduct(credit);
        return credit;
    }

    public Loan createLoan(Customer owner, AAccount account, Period period, double amount) throws InvalidInputException {
        checkCustomer(owner);
        checkProduct(owner, account);
        checkAmount(amount);
        checkPeriod(period.normalized());
        String id = "LOA" + generateProductID();
        Loan loan = new Loan(id, this, account, period.normalized(), amount);
        owner.addProduct(loan);
        return loan;
    }

    public Deposit createDeposit(Customer owner, AAccount account, Period period, double amount) throws InvalidInputException {
        checkCustomer(owner);
        checkProduct(owner, account);
        checkAmount(amount);
        checkPeriod(period.normalized());
        String id = "DEP" + generateProductID();
        Deposit deposit = new Deposit(id, this, account, period.normalized(), amount);
        owner.addProduct(deposit);
        return deposit;
    }

    public AAccount extendAccountWithDebit(Customer owner, AAccount account, double limit) throws InvalidInputException {
        checkCustomer(owner);
        checkProduct(owner, account);
        checkAmount(limit);
        DebitDecorator debit = new DebitDecorator(account, limit);
        owner.addProduct(debit);
        accounts.put(debit.getId(), debit);
        return debit;
    }

    public boolean changeInterest(int customerID, String productID, AInterestStrategy strategy) {
        Customer customer = customers.get(customerID);
        AProduct product = customer.getProduct(productID);
        return new ChangeInterestCommand(product, strategy)
            .execute();
    }

    public void calculateInterest() {
        for (Customer customer: customers.values()) {
            for (AProduct product: customer.getProducts().values()) {
                new CalculateInterestCommand(product)
                    .execute();
            }
        }
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

    private void checkProduct(Customer owner, AProduct product) throws InvalidProductException {
        if(owner.getProduct(product.getId()) == null) {
            throw new InvalidProductException(product.getId());
        }
    }

    private static void checkAmount(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException();
        }
    }

    private static void checkPeriod(Period period) throws InvalidPeriodException {
        if (period.isNegative() || period.isZero()) {
            throw new InvalidPeriodException();
        }
    }

    private static String generateProductID() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "")
                .toUpperCase(Locale.ENGLISH);
    }

}
