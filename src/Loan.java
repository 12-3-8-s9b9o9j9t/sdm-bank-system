import java.util.Date;

public class Loan extends Account {
    private Date startDate;
    private Date endDate;
    private double interestRate;

    public Loan(Date startDate, Date endDate, double interestRate) {
        //TODO
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public double getInterestRate() {
        return interestRate;
    }
}
