package bank.transaction.transfer;

import bank.transaction.ICommand;

public abstract class ATransferState implements ICommand {

    private TransferCommand context;

    public ATransferState(TransferCommand context) {
        this.context = context;
    }

    protected TransferCommand getContext() {
        return context;
    }

    abstract public boolean execute();
    
}
