package bank.transaction.transfer;

public class VerifyAmountState extends ATransferState {

    public VerifyAmountState(TransferCommand context) {
        super(context);
    }

    @Override
    public boolean execute() {
        if (getContext().getAmount() > getContext().getSendingAccount().getBalance()) {
            return false;
        }
        getContext().changeState(new WaitAuthorizationState(getContext()));
        getContext().getSender().addTransferToAuthorize(getContext());
        return true;
    }

}
