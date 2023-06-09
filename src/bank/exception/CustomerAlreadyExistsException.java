package bank.exception;

public class CustomerAlreadyExistsException extends Exception {

    public CustomerAlreadyExistsException(String id) {
        super("Customer with provided id (" + id + ") already exists");
    }
    
}
