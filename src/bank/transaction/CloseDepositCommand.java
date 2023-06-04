package bank.transaction;

import bank.product.Deposit;

public class CloseDepositCommand extends ATransactionCommand {

    private Deposit deposit;

    public CloseDepositCommand(Deposit deposit) {
        super("Close Deposit", "Closing Deposit");
        this.deposit = deposit;
    }

    @Override
    public void execute() {
        deposit.close();
        deposit.log(this);
    }

}
