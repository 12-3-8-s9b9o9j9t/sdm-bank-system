import java.util.Date;

public class PaymentCard {
    private int id;
    private String cardNumber;
    private Date expiryDate;
    private String cvv;
    private Client client;
    private DebitAccount account;

    public PaymentCard(int id, String cardNumber, Date expiryDate, String cvv, DebitAccount account) {
        //TODO
    }

    public int getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public Account getAccount() {
        return account;
    }

    public void updatePaymentCard(PaymentCard paymentCard) {
        //TODO
    }

    public void pay(double amount) {
        //TODO
    }
}
