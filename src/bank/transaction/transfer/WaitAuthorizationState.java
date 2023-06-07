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
        TransferCommand context = getContext();
        if (LocalTime.now().isAfter(time.plusMinutes(MAX_TIME))) {
            return false;
        }
        context.changeState(new WaitReceivingAccountState(context))
            .getSenderBank().addPendingTransfer(context);
        return true;
    }

    @Override
    public double getValue() {
        return 0;
    }
    
}
