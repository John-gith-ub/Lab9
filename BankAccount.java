import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.io.PrintWriter;
import java.time.LocalDate;

//Create the Abstract Class
public abstract class BankAccount {
    protected int accountNumber;
    protected String accountType; // C, S, CD
    protected String firstName;
    protected String lastName;
    protected String transactionType;
    protected String transactionDate;
    protected double transactionAmount;
    protected double accountBalance;
    protected double interestEarned;

    // Default Constructor
    public BankAccount() {
        this.accountNumber = 0;
        this.accountType = "";
        this.firstName = "";
        this.lastName = "";
        this.transactionType = "";
        this.transactionDate = "";
        this.transactionAmount = 0;
        this.accountBalance = 0;
        this.interestEarned = 0;
    }

    // Overloaded constructor
    public BankAccount(int accountNumber, String accountType, String firstName,
            String lastName,
            String transactionType, String transactionDate,
            double transactionAmount, double accountBalance,
            double interestEarned) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.firstName = firstName;
        this.lastName = lastName;
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.transactionAmount = transactionAmount;
        this.accountBalance = accountBalance;
        this.interestEarned = interestEarned;
    }

    // Getters
    // Complete as needed
    public int getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public double getInterestEarned() {
        return interestEarned;
    }

    // Setters
    public void setAccountNumber(int accountnumber) {
        this.accountNumber = accountnumber;
    }

    public void setAccountType(String accounttype) {
        this.accountType = accounttype;
    }

    public void setFirstName(String firstname) {
        this.firstName = firstname;
    }

    public void setLastName(String lastname) {
        this.lastName = lastname;
    }

    public void setTransactionType(String transfertype) {
        this.transactionType = transfertype;
    }

    public void setTransactionDate(String transferdate) {
        this.transactionDate = transferdate;
    }

    public void setTransactionAmount(int transferamount) {
        this.transactionAmount = transferamount;
    }

    public void setAccountBalance(double balance) {
        this.accountBalance = balance;
    }

    public void setInterestEarned(double interest) {
        this.interestEarned = interest;
    }

    // Print formatted account details
    public void printDetails(PrintWriter out) {
        out.printf("%-20d%-20s%-20s%-15s%10.2f%15.2f%n",
                accountNumber, accountType, firstName, lastName,
                accountBalance, interestEarned);
    }

    public void processDeposit(double amt) {
        // add logic for this function
        if (amt <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }
        accountBalance += amt;
        setAccountBalance(accountBalance);

    }

    public void processWithdrawal(double amt) {
        // add logic for this function
        if (amt <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (amt > accountBalance) {
            throw new IllegalArgumentException("Withdrawal amount is greater than current account balance");
        }
        accountBalance -= amt;
        setAccountBalance(accountBalance);
    }

    public void processWithdrawal(double amt, LocalDate currentDate) {
        processWithdrawal(amt);
    }

    public void processInterestEarned(double balance, double rate) {
        double rateamount = balance * (rate / 100.0);
        this.interestEarned += rateamount;
        this.accountBalance += rateamount;
    }
}
