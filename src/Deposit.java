public class Deposit extends Transaction {
    private String place;
    private int atmId;
    private int toAccountId;

    public Deposit(String place, int atmId, int toAccountId) {
        super(0, 0, null);
        //TODO
    }    
}
