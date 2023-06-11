package bank.transaction.transfer;

import bank.exception.InvalidTransactionException;
import bank.product.account.AAccount;

public class WaitReceivingAccountState extends ATransferState {

    public WaitReceivingAccountState(TransferCommand context) {
        super(context);
    }

    @Override
    public boolean execute() {
        TransferCommand context = getContext();
        AAccount receiving = context.getReceivingAccount();
        if (receiving == null || !receiving.getId().equals(context.getReceivingAccountID()) ) {
            return false;
        }
        try { // no exception should be thrown
            double amount = context.getAmount();
            AAccount sending = context.getSendingAccount();
            sending.charge(amount);
            TransferCommand sent = context.copy();
            sending.log(sent.changeState(new TransferSentState(sent)));
            receiving.supply(amount);
            TransferCommand received = context.copy();
            receiving.log(received.changeState(new TransferReceivedState(received)));
        } catch (InvalidTransactionException e) {
            throw new RuntimeException("Unexpected InvalidTransactionException occured", e);
        }
        return true;
    }

    @Override
    public double getValue() {
        return 0;
    }
}
