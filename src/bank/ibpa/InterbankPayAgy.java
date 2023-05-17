package bank.ibpa;

import java.util.HashSet;
import java.util.Set;

import bank.Bank;

public class InterbankPayAgy implements BankMediator {

    private static Set<Bank> banks = new HashSet<>();

    public void notify(Bank sender, String event) {
        switch (event) {
        case "register":
            registerBank(sender);
            break;
        }
    }

    private boolean registerBank(Bank bank) {
        return banks.add(bank);
    }

    private void transfer() {
        // TODO Auto-generated method stub
    }

}