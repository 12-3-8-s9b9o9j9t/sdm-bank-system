package bank.exception;

public class InvalidProductException extends InvalidInputException {

    public InvalidProductException(String productID) {
        super("Customer does not own product " + productID + " or product does not exist");
    }
    
}
