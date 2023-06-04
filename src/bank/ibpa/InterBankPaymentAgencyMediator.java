package bank.ibpa;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import bank.Bank;
import bank.exception.BankAlreadyRegisteredAtIBPAException;

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
    public void notify(Bank sender, String event) {
        switch (event) {
        case "register":
            if (banks.containsValue(sender)) {
                throw new BankAlreadyRegisteredAtIBPAException(sender.getName(), name);
            }
            registerBank(sender);
            break;
        }
    }

    private void registerBank(Bank bank) {
        String ID = generateBankID(bank.getName());
        bank.addIBPA(ID, this);
        banks.put(ID, bank);
    }

    private String generateBankID(String name) {
        Random random = new Random();
        return name.toUpperCase(Locale.ENGLISH) + (random.nextInt() & Integer.MAX_VALUE);
    }

    private void transfer() {
        // TODO Auto-generated method stub
    }

}