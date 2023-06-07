package bank.transaction;

import java.util.function.Predicate;

import bank.product.Product;
import bank.reporter.IVisitor;

public class ReportCommand extends ATransactionCommand {

    private String buffer;
    private Predicate<Product> filter;
    private IVisitor visitor;

    public ReportCommand(String buffer, Predicate<Product> filter) {
        super("Report", "Reporting");
        this.buffer = buffer;
        this.filter = filter;
    }

    @Override
    public boolean execute() {
        if (filter != null) {
            //TODO
        } else {
            //TODO
        }
        return true;
    }

    @Override
    public double getValue() {
        return 0;
    }
    
}
