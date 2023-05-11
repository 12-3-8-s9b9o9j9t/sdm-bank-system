public class Account {
    private int id;
    private double balance;
    private String accountNumber;
    private Client client;
    private TransactionHistory transactionHistory;

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void withdraw(double amount) {
        //TODO
    }

    public void deposit(double amount) {
        //TODO
    }

    public void transfer(double amount, Account account) {
        //TODO
    }

    public TransactionHistory getTransactionHistory() {
        return transactionHistory;
    }
}
