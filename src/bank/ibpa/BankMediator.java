package bank.ibpa;

import bank.Bank;

public interface BankMediator {

    public void notify(Bank sender, String event);
}
