package bank.exception;

public class InvalidTransactionException extends Exception {

    public InvalidTransactionException(String info, String productID) {
        super("Transaction " + info + " not possible for product " + productID);
    }
    
}
