package bank.ibpa;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import bank.Bank;

public class IntBnkPmtAgy {

    private static Map<String, Bank> banks = new HashMap<String, Bank>();

    public void notify(Bank sender, String event) {
        switch (event) {
        case "register":
            if (banks.containsValue(sender)) {
                throw new RuntimeException("Bank already registered");
            }
            registerBank(sender);
            break;
        }
    }

    private void registerBank(Bank bank) {

        String ID = generateID(bank.getName());
        bank.addIBPA(ID, this);
        banks.put(ID, bank);
    }

    private String generateID(String name) {
        Random random = new Random();
        return name.toUpperCase(Locale.ENGLISH) + (random.nextInt() & Integer.MAX_VALUE);
    }

    private void transfer() {
        // TODO Auto-generated method stub
    }

}