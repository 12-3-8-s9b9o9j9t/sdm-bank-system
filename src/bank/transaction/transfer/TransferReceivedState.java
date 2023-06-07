package bank.transaction.transfer;

public class TransferReceivedState extends ATransferState {

    public TransferReceivedState(TransferCommand context) {
        super(context);
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public double getValue() {
        return getContext().getAmount();
    }
    
}
