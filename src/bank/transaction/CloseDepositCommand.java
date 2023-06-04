package bank.transaction;

import bank.product.Deposit;

public class CloseDepositCommand extends ATransactionCommand {

    private Deposit deposit;

    public CloseDepositCommand(Deposit deposit) {
        super("Close Deposit", "Closing Deposit");
        this.deposit = deposit;
    }

    @Override
    public boolean execute() {
        deposit.close();
        deposit.log(this);
        return true;
    }

}
