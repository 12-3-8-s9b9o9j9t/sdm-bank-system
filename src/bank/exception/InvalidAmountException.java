package bank.exception;

public class InvalidAmountException extends Exception {

    public InvalidAmountException() {
        super("Amount must be strictly positive");
    }
    
}
