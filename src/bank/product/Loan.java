package bank.product;

import java.time.LocalDate;
import java.time.Period;

import bank.Bank;
import bank.exception.InvalidTransactionException;
import bank.interest.FixedInterestStrategy;
import bank.product.account.AAccount;
import bank.transaction.ChargeProductCommand;
import bank.transaction.SupplyProductCommand;
import bank.reporter.IVisitor;

public class Loan extends AProduct implements ISuppliable {

    private AAccount account;
    private LocalDate targetDate;
    private double amount;

    public Loan(String id, Bank bank, AAccount account, Period period, double amount) {
        super(id, bank);
        this.targetDate = getCreationDate()
            .plus(period);
        this.account = account;
        this.amount = amount;
        new SupplyProductCommand(account, amount)
            .execute();
        setInterest(new FixedInterestStrategy());
    }

    public AAccount getAccount() {
        return account;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    @Override
    public void supply(double amount) throws InvalidTransactionException {
        if (amount > 0 && new ChargeProductCommand(account, amount)
            .execute()) {
            this.amount -= amount;
        }
        else {
            throw new InvalidTransactionException("repay " + amount, getId());
        }
    }

    @Override
    public double getBalance() {
        return amount;
    }

    @Override
    public double calculateInterest() {
        double interest = getInterest()
            .calculate(this);
        amount += interest;
        return -interest;
    }

    @Override
    public String accept(IVisitor visitor) {
        return visitor.visitLoan(this);
    }
}
