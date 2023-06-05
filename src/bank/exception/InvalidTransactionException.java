package bank.exception;

public class InvalidTransactionException extends Exception {

    public InvalidTransactionException(String info, String reason) {
        super("Transaction " + info + " not possible because " + reason);
    }
    
}
