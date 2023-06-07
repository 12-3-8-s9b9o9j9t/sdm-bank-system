package bank.transaction.transfer;

public class VerifyAmountState extends ATransferState {

    public VerifyAmountState(TransferCommand context) {
        super(context);
    }

    @Override
    public boolean execute() {
        TransferCommand context = getContext();
        if (context.getAmount() > context.getSendingAccount().getBalance()) {
            return false;
        }
        context.changeState(new WaitAuthorizationState(context))
            .getSender().addTransferToAuthorize(context);
        return true;
    }

    @Override
    public double getValue() {
        return 0;
    }

}
