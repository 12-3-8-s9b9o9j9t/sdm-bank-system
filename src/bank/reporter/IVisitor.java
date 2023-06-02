package bank.reporter;

import bank.Customer;
import bank.product.Product;
import bank.transaction.ATransactionCommand;

public interface IVisitor {

    public void visitCustomer(Customer customer);

    public void visitProduct(Product product);

    public void visitTransaction(ATransactionCommand transaction);
    
}
