package bank.ibpa;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import bank.Bank;
import bank.exception.BankAlreadyRegisteredAtIbpaException;
import bank.exception.InvalidBankException;
import bank.transaction.transfer.TransferCommand;

public class InterBankPaymentAgencyMediator implements IMediator {

    private Map<String, Bank> banks = new HashMap<String, Bank>();
    private String name;

    public InterBankPaymentAgencyMediator(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void notify(Bank sender, String event) throws BankAlreadyRegisteredAtIbpaException, InvalidBankException {
        if (event.equals("register")) {
            if (banks.containsValue(sender)) {
                throw new BankAlreadyRegisteredAtIbpaException(sender.getName(), name);
            }
            registerBank(sender);    
        } else {
            checkBank(sender);
            // switch for other events
        }
    }

    private void registerBank(Bank bank) {
        String id = generateBankID(bank.getName());
        bank.addIbpa(id, this);
        banks.put(id, bank);
    }

    private String generateBankID(String name) {
        Random random = new Random();
        return name.toUpperCase(Locale.ENGLISH)
            .replaceAll("[\\s-]", "")
             + (random.nextInt() & Integer.MAX_VALUE);
    }

    public void transfer() {
        for (Bank bank: banks.values()) {
            List<TransferCommand> transfers =  bank.getPendingTransfers(name);
            for (TransferCommand transfer: transfers) {
                Bank receiving = banks.get(transfer.getReceivingBankID());
                if (receiving != null) {
                    receiving.addPendingTransfer(transfer);
                } else {
                    transfer.execute();
                }
            }
            transfers.clear();
        }
    }

    private void checkBank(Bank bank) throws InvalidBankException {
        if (!banks.containsValue(bank)) {
            throw new InvalidBankException(bank.getName());
        }
    }

}