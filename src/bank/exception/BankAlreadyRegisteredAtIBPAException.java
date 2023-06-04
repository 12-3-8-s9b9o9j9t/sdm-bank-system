package bank.exception;

public class BankAlreadyRegisteredAtIBPAException extends RuntimeException {

    public BankAlreadyRegisteredAtIBPAException(String bankName, String ibpaName) {
        super(String.format("Bank %s already registered at IBPA %s", bankName, ibpaName));
    }
    
}
