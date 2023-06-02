package bank.interest;

import java.time.LocalDate;
import java.time.Period;

public class Interest {

    private State state;

    public LocalDate starDate;
    public LocalDate endDate;

    public Interest(State _state) {
        state = _state;
    }

    public void setState(State _state) {
        state = _state;
    }

    public double calculate(LocalDate starDate, LocalDate endDate) {
        return state.calculate(starDate, endDate);
    }

    public void extendDate(LocalDate _endDate) {
        endDate = _endDate;
    }

    public int getDurationInYears() {
        Period period = Period.between(starDate, endDate)
            .normalized();
        return period.getYears() + period.getMonths() / 12 + period.getDays() / 365;
    }
}
