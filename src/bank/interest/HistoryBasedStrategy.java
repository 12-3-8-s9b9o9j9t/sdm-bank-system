package bank.interest;

import java.time.LocalDate;
import java.util.List;

import bank.product.Product;
import bank.transaction.ATransactionCommand;
import bank.transaction.ChargeProductCommand;
import bank.transaction.SupplyProductCommand;

public class HistoryBasedStrategy extends AInterestStrategy {

    private double rate;

    public HistoryBasedStrategy() {
        this(STANDARD_RATE);
    }

    public HistoryBasedStrategy(double rate) {
        this.rate = rate;
    }

    @Override
    public double calculate(Product product) {
        if (getLastCalculationDate() == null) {
            setLastCalculationDate(product.getCreationDate());
        }
        double modifier = 1;
        List<ATransactionCommand> history = product.getHistory(getLastCalculationDate());

        for (ATransactionCommand transaction : history) {
            if (transaction instanceof SupplyProductCommand) {
                modifier *= 0.9;
            }
            else if (transaction instanceof ChargeProductCommand) {
                modifier *= 1.1;
            }
        }

        LocalDate now = LocalDate.now();
        double interest = product.getBalance() * rate * modifier
            * getDurationInYears(getLastCalculationDate(), now);
        setLastCalculationDate(now);
        return interest;
    }
    
}
