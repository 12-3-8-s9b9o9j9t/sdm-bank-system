package bank.exception;

public class CustomerAlreadyExistsException extends Exception {

    public CustomerAlreadyExistsException(String ID) {
        super("Customer with provided ID (" + ID + ") already exists");
    }
    
}
