package bank.interest;

import java.sql.Date;

public class Interest {

    private State state;

    public int id;
    public Date starDate;
    public Date endDate;

    public Interest(State _state, int _id) {
        state = _state;
        id = _id; 
    }

    public void setState(State _state) {
        state = _state;
    }

    public double calculate(Date starDate, Date endDate) {
        return state.calculate(starDate, endDate);
    }

    public void extendDate(Date _endDate) {
        endDate = _endDate;
    }

    public int getDurationInYears() {
        return (int) ((endDate.getTime() - starDate.getTime()) / (1000 * 60 * 60 * 24 * 365));
    }
}
