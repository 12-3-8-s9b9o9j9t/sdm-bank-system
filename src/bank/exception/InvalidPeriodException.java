package bank.exception;

public class InvalidPeriodException extends InvalidInputException {

    public InvalidPeriodException() {
        super("Period must be strictly positive");
    }
    
}
