package bank.exception;

public class InvalidProductException extends RuntimeException {

    public InvalidProductException(String productID) {
        super("Customer does not own product " + productID + " or product does not exist");
    }
    
}
