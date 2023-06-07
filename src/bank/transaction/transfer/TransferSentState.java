package bank.transaction.transfer;

public class TransferSentState extends ATransferState {

    public TransferSentState(TransferCommand context) {
        super(context);
    }

    @Override
    public boolean execute() {
        return false;
    }

    @Override
    public double getValue() {
        return -getContext().getAmount();
    }
    
}
