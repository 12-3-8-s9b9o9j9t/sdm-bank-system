package bank.transaction;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Predicate;
import java.util.stream.Stream;

import bank.Customer;
import bank.product.Product;
import bank.reporter.IVisitor;
import bank.reporter.ReporterVisitor;

public class ReportCommand extends ATransactionCommand {

    private String buffer;
    private Predicate<Product> filter;
    private IVisitor visitor = new ReporterVisitor();
    private Customer customer;

    public ReportCommand(String buffer, Predicate<Product> filter, Customer customer) {
        super("Report", "Reporting");
        this.buffer = buffer;
        this.filter = filter;
        this.customer = customer;
    }

    @Override
    public boolean execute() {
        StringBuilder sb = new StringBuilder();
        sb.append(customer.accept(visitor));
        sb.append('\n');
        Stream<Product> ps = customer.getProducts().values().stream();
        if (filter != null) {
            ps = ps.filter(filter);
        }
        ps.forEach(p -> 
            {
                sb.append(p.accept(visitor));
                sb.append('\n');
                sb.append("Last month's transactions:\n");
                p.getHistory(LocalDate.now().minus(Period.ofMonths(1)))
                    .forEach(t -> sb.append(t.accept(visitor)));
                sb.append("\n\n");
            });
        buffer = sb.toString();
        return true;
    }

    @Override
    public double getValue() {
        return 0;
    }
    
}
