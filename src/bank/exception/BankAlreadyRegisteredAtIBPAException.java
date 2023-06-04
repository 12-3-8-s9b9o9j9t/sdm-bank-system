package bank.exception;

public class BankAlreadyRegisteredAtIBPAException extends RuntimeException {

    public BankAlreadyRegisteredAtIBPAException(String bankName, String ibpaName) {
        super("Bank " + bankName + " already registered at IBPA " + ibpaName);
    }
    
}
