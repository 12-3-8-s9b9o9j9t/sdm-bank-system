package bank.exception;

public class BankAlreadyRegisteredAtIBPAException extends Exception {

    public BankAlreadyRegisteredAtIBPAException(String bankName, String ibpaName) {
        super("Bank " + bankName + " already registered at IBPA " + ibpaName);
    }
    
}
