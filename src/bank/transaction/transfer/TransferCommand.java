package bank.transaction.transfer;

import java.util.UUID;

import bank.Bank;
import bank.Customer;
import bank.product.account.AAccount;
import bank.transaction.ATransactionCommand;

public class TransferCommand extends ATransactionCommand {

    private Bank senderBank;
    private Customer sender;
    private String ID = generateID();
    private AAccount sendingAccount;
    private String receivingAccountID;
    private AAccount receivingAccount = null;
    private String receivingBankID = null;
    private String IBPAName = null;
    private double amount;

    private ATransferState state;

    public TransferCommand(Bank senderBank, Customer sender, AAccount sendingAccount, String receivingAccountID, double amount) {
        super("Transfer", "Transfering");
        this.senderBank = senderBank;
        this.sender = sender;
        this.sendingAccount = sendingAccount;
        this.receivingAccountID = receivingAccountID;
        this.amount = amount;
    }

    public TransferCommand(Bank senderBank, Customer sender, AAccount sendingAccount, String receivingAccountID, String receivingBankID, String IBPAName, double amount) {
        this(senderBank, sender, sendingAccount, receivingAccountID, amount);
        this.receivingBankID = receivingBankID;
        this.IBPAName = IBPAName;
    }

    public String getID() {
        return ID;
    }

    Bank getSenderBank() {
        return senderBank;
    }

    Customer getSender() {
        return sender;
    }

    AAccount getSendingAccount() {
        return sendingAccount;
    }

    AAccount getReceivingAccount() {
        return receivingAccount;
    }

    public void setReceivingAccount(AAccount receivingAccount) {
        this.receivingAccount = receivingAccount;
    }

    public String getSendingAccountID() {
        return sendingAccount.getID();
    }

    public String getReceivingAccountID() {
        return receivingAccountID;
    }

    public String getReceivingBankID() {
        return receivingBankID;
    }

    public String getIBPAName() {
        return IBPAName;
    }

    double getAmount() {
        return amount;
    }

    @Override
    public boolean execute() {
        if (!state.execute()) {
            setDescription(getDescription() + " : Failed");
            sendingAccount.log(this);
            return false;
        }
        return true;
    }

    public void changeState(ATransferState state) {
        this.state = state;
    }

    private String generateID() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "");
    }

}
