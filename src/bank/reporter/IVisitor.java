 package bank.reporter;

import bank.Customer;
import bank.product.Credit;
import bank.product.Deposit;
import bank.product.Loan;
import bank.product.account.BaseAccount;
import bank.product.account.DebitDecorator;
import bank.transaction.ATransactionCommand;

public interface IVisitor {

    public String visitCustomer(Customer customer);

    public String visitLoan(Loan loan);

    public String visitDeposit(Deposit deposit);

    public String visitCredit(Credit credit);

    public String visitBaseAccount(BaseAccount baseAccount);

    public String visitDebitAccount(DebitDecorator debitAccount);

    public String visitTransaction(ATransactionCommand transaction);
    
}
