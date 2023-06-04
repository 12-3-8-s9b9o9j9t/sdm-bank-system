package bank.exception;

public class OperationNotAffordableException extends Exception {

    public OperationNotAffordableException(String info, String productID) {
        super("Operation " + info + " not affordable for product " + productID);
    }
    
}
