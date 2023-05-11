import java.util.Date;
import java.util.List;

public class Client {
    private int id;
    private String name;
    private String surname;
    private String address;
    private String email;
    private List<Account> accounts;

    public Client(int id, String name, String surname, String address, String email) {
        //TODO
    }

    public int getId() {
        return id;
    }

    public void updateClient(Client client) {
        //TODO
    }

    public void openDebitAccount() {
        //TODO
    }

    public void openDepositAccount(Date endDate, InterestRate interestRate) {
        //TODO
    }

    public void openLoanAccount(Date startDate, Date endDate, InterestRate interestRate) {
        //TODO
    }
}
