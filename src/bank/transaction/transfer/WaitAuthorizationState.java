package bank.transaction.transfer;

import java.time.LocalDateTime;

public class WaitAuthorizationState extends ATransferState {

    private static final int MAX_TIME_MINUTES = 5;

    private LocalDateTime time = LocalDateTime.now();

    public WaitAuthorizationState(TransferCommand context) {
        super(context);
    }

    @Override
    public boolean execute() {
        TransferCommand context = getContext();
        if (LocalDateTime.now().isAfter(time.plusMinutes(MAX_TIME_MINUTES))) {
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
