package bank;
import java.util.ArrayList;
import java.util.List;

import bank.product.Product;

public class Customer {

    private String ID; // the national ID of the customer
    private String name;
    private Bank bank;
    private List<Product> products = new ArrayList<Product>();

    public Customer(String ID, String name, Bank bank) {
        this.ID = ID;
        this.name = name;
        this.bank = bank;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    //The Customer is defined by its ID and Bank
    //override the hash method to use ID and Bank
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + bank.hashCode();
        result = prime * result + ID.hashCode();
        return result;
    }

    //override the equals method to use ID and Bank
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Customer) {
            Customer customer = (Customer) obj;
            return ID.equals(customer.ID) && bank.equals(customer.bank);
        }
        return false;
    }
    
}
