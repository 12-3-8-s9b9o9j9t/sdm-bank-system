import java.util.Date;

public class Transaction {
    private int id;
    private double amount;
    private Date date;

    public Transaction(int id, double amount, Date date) {
        this.id = id;
        this.amount = amount;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }
}