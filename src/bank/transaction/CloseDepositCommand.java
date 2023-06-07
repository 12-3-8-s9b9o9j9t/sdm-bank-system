package bank.transaction;

import bank.exception.InvalidTransactionException;
import bank.product.Deposit;

public class CloseDepositCommand extends ATransactionCommand {

    private Deposit deposit;

    public CloseDepositCommand(Deposit deposit) {
        super("Close Deposit", "Closing Deposit");
        this.deposit = deposit;
    }

    @Override
    public boolean execute() {
        try {
            deposit.close();
        } catch (InvalidTransactionException e) {
            setDescription(getDescription() + " : Failed");
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
