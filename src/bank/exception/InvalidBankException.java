package bank.exception;

public class InvalidBankException extends InvalidInputException {

    public InvalidBankException(String bankName) {
        super("The bank " + bankName + " is not registered at the ibpa");
    }
    
}
