package bank.exception;

public class InvalidAmountException extends InvalidInputException {

    public InvalidAmountException() {
        super("Amount must be strictly positive");
    }
    
}
