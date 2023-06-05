package bank.exception;

public class InvalidBankException extends Exception {

    public InvalidBankException(String bankName) {
        super("The bank " + bankName + " is not registered at the IBPA");
    }
    
}
