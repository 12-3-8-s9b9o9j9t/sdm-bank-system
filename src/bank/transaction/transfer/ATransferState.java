package bank.transaction.transfer;

import bank.transaction.ICommand;
import bank.transaction.IValuable;

public abstract class ATransferState implements ICommand, IValuable {

    private TransferCommand context;

    public ATransferState(TransferCommand context) {
        this.context = context;
    }

    protected TransferCommand getContext() {
        return context;
    }
    
}
