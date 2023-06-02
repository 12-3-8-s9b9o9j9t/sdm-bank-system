package bank.product;
public class Credit extends Product {
    
    private double limit;
    private double amount;

    public Credit(double limit) {
        this.limit = limit;
        setInterest(null /* TODO */);
    }

    public void borrow(double amount) {
        // TODO Auto-generated method stub

    }

    public void repay(double amount) {
        // TODO Auto-generated method stub

    }

}
