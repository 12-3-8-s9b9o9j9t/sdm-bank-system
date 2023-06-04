package bank.exception;

public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String ID) {
        super(String.format("Customer with provided ID (%s) already exists", ID));
    }
    
}
