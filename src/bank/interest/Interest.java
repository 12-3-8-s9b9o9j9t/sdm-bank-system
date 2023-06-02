package bank.interest;

import java.time.LocalDate;
import java.time.Period;

public abstract class Interest {

    public abstract double calculate(LocalDate starDate, LocalDate endDate);

    protected static double getDurationInYears(LocalDate starDate, LocalDate endDate) {
        Period period = Period.between(starDate, endDate)
            .normalized();
        return period.getYears() + (double)period.getMonths() / 12 + (double)period.getDays() / 365;
    }

}
