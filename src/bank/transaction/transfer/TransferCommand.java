package bank.transaction.transfer;

import java.util.UUID;

import bank.Bank;
import bank.Customer;
import bank.product.account.AAccount;
import bank.transaction.ATransactionCommand;

public class TransferCommand extends ATransactionCommand {

    private Bank senderBank;
    private Customer sender;
    private String id;
    private AAccount sendingAccount;
    private String receivingAccountID;
    private AAccount receivingAccount = null;
    private String receivingBankID = null;
    private String IbpaName = null;
    private double amount;
    private ATransferState state;

    public TransferCommand(Bank senderBank, Customer sender, AAccount sendingAccount, String receivingAccountID, double amount) {
        super("Transfer",
            new StringBuilder("Transfering ")
                .append(amount)
                .append(" from Account ")
                .append(sendingAccount.getId())
                .append(" to Account ")
                .append(receivingAccountID).toString());
        this.senderBank = senderBank;
        this.sender = sender;
        this.id = generateID();
        this.sendingAccount = sendingAccount;
        this.receivingAccountID = receivingAccountID;
        this.amount = amount;
        this.state = new VerifyAmountState(this);
    }

    public TransferCommand(Bank senderBank, Customer sender, AAccount sendingAccount, String receivingAccountID, String receivingBankID, String IbpaName, double amount) {
        super("Transfer",
            new StringBuilder("Transfering ")
                    .append(amount)
                    .append(" from Account ")
                    .append(sendingAccount.getId())
                    .append(" to Account ")
                    .append(receivingAccountID)
                    .append(" at Bank ")
                    .append(receivingBankID)
                    .append(" with ibpa ")
                    .append(IbpaName).toString());
        this.senderBank = senderBank;
        this.sender = sender;
        this.id = generateID();
        this.sendingAccount = sendingAccount;
        this.receivingAccountID = receivingAccountID;
        this.amount = amount;
        this.receivingBankID = receivingBankID;
        this.IbpaName = IbpaName;
        this.state = new VerifyAmountState(this);
    }

    private TransferCommand(TransferCommand toCopy) {
        super("Transfer",
            toCopy.getDescription());
        this.senderBank = toCopy.senderBank;
        this.sender = toCopy.sender;
        this.id = toCopy.id;
        this.sendingAccount = toCopy.sendingAccount;
        this.receivingAccountID = toCopy.receivingAccountID;
        this.amount = toCopy.amount;
        this.receivingBankID = toCopy.receivingBankID;
        this.IbpaName = toCopy.IbpaName;
    }

    public String getId() {
        return id;
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
        return sendingAccount.getId();
    }

    public String getReceivingAccountID() {
        return receivingAccountID;
    }

    public String getReceivingBankID() {
        return receivingBankID;
    }

    public String getIbpaName() {
        return IbpaName;
    }

    double getAmount() {
        return amount;
    }

    @Override
    public boolean execute() {
        if (!state.execute()) {
            setDescription(getDescription() + ": Failed");
            sendingAccount.log(this);
            return false;
        }
        return true;
    }

    @Override
    public double getValue() {
        return state.getValue();
    }

    public TransferCommand changeState(ATransferState state) {
        this.state = state;
        return this;
    }

    private String generateID() {
        return UUID.randomUUID()
                .toString()
                .replaceAll("-", "");
    }

    TransferCommand copy() {
        return new TransferCommand(this);
    }

}
