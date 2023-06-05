package bank.exception;

public class InvalidCustomerException extends Exception {

    public InvalidCustomerException(String bankName) {
        super("Customer is not registered at bank " + bankName);
    }
    
}
