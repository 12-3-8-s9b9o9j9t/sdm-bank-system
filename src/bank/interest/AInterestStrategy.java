package bank.interest;

import java.time.LocalDate;

import bank.product.Product;

public abstract class AInterestStrategy {

    public static final double STANDARD_RATE = 0.01;
    public static final double LOW_RATE = 0.005;
    public static final double HIGH_RATE = 0.02;

    private LocalDate lastCalculationDate = null;

    public abstract double calculate(Product product);

    public LocalDate getLastCalculationDate() {
        return lastCalculationDate;
    }

    protected void setLastCalculationDate(LocalDate lastCalculationDate) {
        this.lastCalculationDate = lastCalculationDate;
    }

    protected static double getDurationInYears(LocalDate startDate, LocalDate endDate) {
        return (endDate.toEpochDay() - startDate.toEpochDay()) / 365.0;
    }

}