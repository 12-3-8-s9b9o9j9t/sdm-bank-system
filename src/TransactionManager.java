import java.util.Date;

public class TransactionManager {
    public Withdrawal createWithdrawal(int id, double amount, Date date, String place, int atmId, Account fromAccount) {
        //TODO
        return null;
    }

    public Deposit createDeposit(int id, double amount, Date date, Account toAccount, String place, int atmId) {
        //TODO
        return null;
    }

    public Transfer createTransfer(int id, double amount, Date date, Account fromAccount, Account toAccount) {
        //TODO
        return null;
    }

    public boolean verifyTransaction(Transaction transaction) {
        //TODO
        return false;
    }
}
