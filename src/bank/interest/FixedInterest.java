package bank.interest;

import bank.product.Product;

public class FixedInterest implements Interest {
    
    private Product context;
    private double rate;

    public FixedInterest(Product context, double rate) {
        this.context = context;
        this.rate = rate;
    }

    public void calculate() {
        // TODO Auto-generated method stub
    }
}
