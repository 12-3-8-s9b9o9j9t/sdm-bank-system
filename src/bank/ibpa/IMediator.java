package bank.ibpa;

import bank.Bank;

public interface IMediator {
    
    public void notify(Bank sender, String event) throws Exception;
}
