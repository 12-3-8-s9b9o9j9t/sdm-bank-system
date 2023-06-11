package bank.transaction;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.Predicate;
import java.util.stream.Stream;

import bank.Customer;
import bank.product.AProduct;
import bank.reporter.IVisitor;
import bank.reporter.ReporterVisitor;

public class ReportCommand extends ATransactionCommand {

    private StringBuilder builder;
    private Predicate<AProduct> filter;
    private IVisitor visitor = new ReporterVisitor();
    private Customer customer;

    public ReportCommand(StringBuilder builder, Predicate<AProduct> filter, Customer customer) {
        super("Report", "Reporting");
        this.builder = builder;
        this.filter = filter;
        this.customer = customer;
    }

    @Override
    public boolean execute() {
        builder.append(customer.accept(visitor));
        builder.append('\n');
        Stream<AProduct> ps = customer.getProducts().values().stream();
        if (filter != null) {
            ps = ps.filter(filter);
        }
        ps.forEach(p -> 
            {
                builder.append(p.accept(visitor));
                builder.append('\n');
                builder.append("Last month's transactions:\n");
                p.getHistory(LocalDate.now().minus(Period.ofMonths(1)))
                    .forEach(t -> builder.append(t.accept(visitor)));
                builder.append("\n\n");
            });
        return true;
    }

    @Override
    public double getValue() {
        return 0;
    }
    
}
