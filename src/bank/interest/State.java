package bank.interest;

import java.time.LocalDate;

public interface State {

    public double calculate(LocalDate starDate, LocalDate endDate);

}
