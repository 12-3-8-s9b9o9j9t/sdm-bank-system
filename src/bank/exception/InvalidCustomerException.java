package bank.exception;

public class InvalidCustomerException extends RuntimeException {

    public InvalidCustomerException(String bankName) {
        super(String.format("Customer is not registered at bank %s", bankName));
    }
    
}
