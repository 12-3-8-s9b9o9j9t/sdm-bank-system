package bank.interest;

import java.time.LocalDate;

import bank.product.AProduct;

public class FixedInterestStrategy extends AInterestStrategy {

    private double rate;

    public FixedInterestStrategy() {
        this(STANDARD_RATE);
    }

    public FixedInterestStrategy(double rate) {
        super("Fixed Interest");
        this.rate = rate;
    }

    @Override
    public double calculate(AProduct product) {
        if (getLastCalculationDate() == null) {
            setLastCalculationDate(product.getCreationDate());
        }
        LocalDate now = LocalDate.now();
        double interest = product.getBalance() * rate
            * getDurationInYears(getLastCalculationDate(), now);
        setLastCalculationDate(now);
        return interest;
    }
    
}
