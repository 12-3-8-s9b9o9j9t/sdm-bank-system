package bank.reporter;

import bank.Customer;
import bank.product.Credit;
import bank.product.Deposit;
import bank.product.Loan;
import bank.product.account.BaseAccount;
import bank.product.account.DebitDecorator;
import bank.transaction.ATransactionCommand;

public class ReporterVisitor implements IVisitor {

    @Override
    public String visitCustomer(Customer customer) {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(customer.getName()).append("\n");
        sb.append("Products: ").append("\n");
        return sb.toString();
    }

    @Override
    public String visitLoan(Loan loan) {
        StringBuilder sb = new StringBuilder();
        sb.append("Loan: ").append("\n");
        sb.append("Account: ").append(loan.getAccount().getID()).append("\n");
        sb.append("Target date: ").append(loan.getTargetDate()).append("\n");
        sb.append("Amount: ").append(loan.getAmount()).append("\n");
        return sb.toString();
    }

    @Override
    public String visitDeposit(Deposit deposit) {
        StringBuilder sb = new StringBuilder();
        sb.append("Deposit: ").append("\n");
        sb.append("Account: ").append(deposit.getAccount().getID()).append("\n");
        sb.append("Target date: ").append(deposit.getTargetDate()).append("\n");
        sb.append("Amount: ").append(deposit.getAmount()).append("\n");
        sb.append("Ceiling: ").append(deposit.getMax()).append("\n");
        return sb.toString();
    }

    @Override
    public String visitCredit(Credit credit) {
        StringBuilder sb = new StringBuilder();
        sb.append("Credit: ").append("\n");
        sb.append("Ceiling: ").append(credit.getLimit()).append("\n");
        sb.append("Amount: ").append(credit.getAmount()).append("\n");
        return sb.toString();
    }

    @Override
    public String visitBaseAccount(BaseAccount baseAccount) {
        StringBuilder sb = new StringBuilder();
        sb.append("Base account: ").append("\n");
        sb.append("ID: ").append(baseAccount.getID()).append("\n");
        sb.append("Open date: ").append(baseAccount.getOpenDate()).append("\n");
        sb.append("Balance: ").append(baseAccount.getBalance()).append("\n");
        return sb.toString();
    }

    @Override
    public String visitDebitAccount(DebitDecorator debitAccount) {
        StringBuilder sb = new StringBuilder(); 
        sb.append("Debit account: ").append("\n");
        sb.append("ID: ").append(debitAccount.getID()).append("\n");
        sb.append("Balance: ").append(debitAccount.getBalance()).append("\n");
        sb.append("Overdraft: ").append(debitAccount.getOverdraft()).append("\n");
        return sb.toString();
    }

    @Override
    public String visitTransaction(ATransactionCommand transaction) {
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction: ").append("\n");
        sb.append("Type: ").append(transaction.getType()).append("\n");
        sb.append("Date: ").append(transaction.getDate()).append("\n");
        sb.append("Description: ").append(transaction.getDescription()).append("\n");
        return sb.toString();
    }    
}
