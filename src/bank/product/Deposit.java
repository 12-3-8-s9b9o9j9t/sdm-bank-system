package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.Bank;
import bank.exception.InvalidTransactionException;
import bank.interest.AInterestStrategy;
import bank.interest.FixedInterestStrategy;
import bank.product.account.AAccount;
import bank.transaction.CalculateInterestCommand;
import bank.transaction.ChargeProductCommand;
import bank.transaction.SupplyProductCommand;
import bank.reporter.IVisitor;

public class Deposit extends AProduct implements ISuppliable {

    private AAccount account;
    private LocalDate targetDate;
    private double limit;
    private double amount = 0;

    public Deposit(String id, Bank bank, AAccount account, Period period, double limit) {
        super(id, bank);
        this.targetDate = getCreationDate()
            .plus(period);
        this.account = account;
        this.limit = limit;
        setInterest(new FixedInterestStrategy(AInterestStrategy.HIGH_RATE));
    }

    public AAccount getAccount() {
        return account;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public double getLimit() {
        return limit;
    }

    @Override
    public void supply(double amount) throws InvalidTransactionException {
        if (amount > 0 && this.amount + amount <= limit
                && new ChargeProductCommand(account, amount)
                    .execute()) {
            this.amount += amount;
        } else {
            throw new InvalidTransactionException("supply " + amount, getId());
        }
    }

    public void close() throws InvalidTransactionException {
        new CalculateInterestCommand(this)
            .execute();
        boolean success = new SupplyProductCommand(account, amount)
                .execute();
        if (!success) {
            throw new InvalidTransactionException("close", getId());
        }
        amount = 0;
    }

    @Override
    public double getBalance() {
        return amount;
    }

    @Override
    public double calculateInterest() {
        double interest = 0;
        if (LocalDate.now()
            .isAfter(targetDate)) {
            interest = getInterest()
                .calculate(this);
            amount += interest;
        }
        return interest;
    }

    @Override
    public String accept(IVisitor visitor) {
        return visitor.visitDeposit(this);
    }
}
