package bank.transaction.transfer;

import java.time.LocalTime;

public class WaitAuthorizationState extends ATransferState {

    private static final int MAX_TIME = 5;

    private LocalTime time = LocalTime.now();

    public WaitAuthorizationState(TransferCommand context) {
        super(context);
    }

    @Override
    public boolean execute() {
        if (LocalTime.now().isAfter(time.plusMinutes(MAX_TIME))) {
            return false;
        }
        getContext().changeState(new WaitReceivingAccountState(getContext()));
        getContext().getSenderBank().addPendingTransfer(getContext());
        return true;
    }
    
}
