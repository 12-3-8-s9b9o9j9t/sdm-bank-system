package bank.transaction;

import java.time.LocalDate;
import java.time.Period;

import bank.exception.InvalidTransactionException;
import bank.product.Deposit;

public class CloseDepositCommand extends ATransactionCommand {

    private Deposit deposit;

    public CloseDepositCommand(Deposit deposit) {
        super("Close Deposit", 
            new StringBuilder("Closing Deposit linked to Account ")
            .append(deposit.getAccount().getID())
            .append(" with an advance of ")
            .append(Period.between(LocalDate.now(), deposit.getTargetDate()))
            .toString());
        this.deposit = deposit;
    }

    @Override
    public boolean execute() {
        try {
            deposit.close();
        } catch (InvalidTransactionException e) {
            setDescription(getDescription() + ": Failed");
            return false;
        }
        deposit.log(this);
        return true;
    }

    @Override
    public double getValue() {
        return 0;
    }

}
