package bank.interest;

import java.sql.Date;

public class Interest {

    private State state;

    public int id;
    public Date starDate;
    public Date endDate;

    public Interest(State _state, int _id, Date _starDate, Date _endDate) {
        state = _state;
        id = _id;
        starDate = _starDate;
        endDate = _endDate;
    }

    public void setState(State _state) {
        state = _state;
    }

    public double calculate() {
        return state.calculate();
    }

    public void extendDate(Date _endDate) {
        endDate = _endDate;
    }

    public int getDurationInYears() {
        return (int) ((endDate.getTime() - starDate.getTime()) / (1000 * 60 * 60 * 24 * 365));
    }
}
