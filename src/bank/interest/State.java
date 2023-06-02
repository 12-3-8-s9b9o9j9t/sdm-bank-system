package bank.interest;

import java.sql.Date;

public interface State {

    public double calculate(Date starDate, Date endDate);

}
