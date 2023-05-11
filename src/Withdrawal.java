public class Withdrawal extends Transaction {
    private String place;
    private int atmId;
    private Account fromAccount;

    public Withdrawal(String place, int atmId, Account fromAccount) {
        super(0, 0, null);
        //TODO
    }
    
}
