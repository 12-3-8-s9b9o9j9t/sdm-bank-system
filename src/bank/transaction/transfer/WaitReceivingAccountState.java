package bank.transaction.transfer;

import bank.product.account.AAccount;

public class WaitReceivingAccountState extends ATransferState {

    public WaitReceivingAccountState(TransferCommand context) {
        super(context);
    }

    @Override
    public boolean execute() {
        TransferCommand context = getContext();
        AAccount receivingAccount = context.getReceivingAccount();
        if (receivingAccount == null || !receivingAccount.getID().equals(context.getReceivingAccountID()) ) {
            return false;
        }
        try { // no exception should be thrown
            double amount = context.getAmount();
            AAccount sending = context.getSendingAccount();
            AAccount receiving = context.getReceivingAccount();
            sending.charge(amount);
            sending.log(context);
            receiving.supply(amount);
            receiving.log(context);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
