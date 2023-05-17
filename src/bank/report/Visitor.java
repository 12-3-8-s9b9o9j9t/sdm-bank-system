package bank.report;

import bank.Customer;
import bank.product.Product;
import bank.transaction.Transaction;

public interface Visitor {

    public void visitCustomer(Customer customer);

    public void visitProduct(Product product);

    public void visitTransaction(Transaction transaction);
    
}
