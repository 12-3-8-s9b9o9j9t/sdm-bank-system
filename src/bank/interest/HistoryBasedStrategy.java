package bank.interest;

import java.time.LocalDate;
import java.util.List;

import bank.product.AProduct;
import bank.transaction.ATransactionCommand;

public class HistoryBasedStrategy extends AInterestStrategy {

    private double rate;

    public HistoryBasedStrategy() {
        this(STANDARD_RATE);
    }

    public HistoryBasedStrategy(double rate) {
        super("History Based Interest");
        this.rate = rate;
    }

    @Override
    public double calculate(AProduct product) {
        if (getLastCalculationDate() == null) {
            setLastCalculationDate(product.getCreationDate());
        }
        double modifier = 1;
        List<ATransactionCommand> history = product.getHistory(getLastCalculationDate());

        double valueChange = 0;

        for (ATransactionCommand command: history) {
            valueChange += command.getValue();
        }

        if (valueChange < 0) {
            modifier = 1.2;
        } else if (valueChange > 0) {
            modifier = 0.8;
        }

        LocalDate now = LocalDate.now();
        double interest = product.getBalance() * rate * modifier
            * getDurationInYears(getLastCalculationDate(), now);
        setLastCalculationDate(now);
        return interest;
    }
    
}
