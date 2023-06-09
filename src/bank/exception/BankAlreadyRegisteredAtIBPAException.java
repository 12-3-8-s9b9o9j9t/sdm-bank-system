package bank.exception;

public class BankAlreadyRegisteredAtIbpaException extends Exception {

    public BankAlreadyRegisteredAtIbpaException(String bankName, String ibpaName) {
        super("Bank " + bankName + " already registered at ibpa " + ibpaName);
    }
    
}
