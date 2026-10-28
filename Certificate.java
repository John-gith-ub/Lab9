import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Certificate extends BankAccount {
    private boolean withdrawalTaken = false;
    private LocalDate lastWithdrawalDate = null;

    public boolean WithdrawalHasBeenTaken() {
        return withdrawalTaken;
    }

    public void setWithdrawalTaken(boolean withdrawalTaken) {
        this.withdrawalTaken = withdrawalTaken;
    }

    public void reset(LocalDate currentDate) {
        if (lastWithdrawalDate != null) {
            long daysPassed = ChronoUnit.DAYS.between(lastWithdrawalDate, currentDate);
            if (daysPassed > 5) {
                withdrawalTaken = false;
            }
        }
    }

    public void setLastWithdrawalDate(LocalDate date) {
        lastWithdrawalDate = date;
    }

    // Overloaded Contructor that uses Super
    public Certificate(int accountNumber, String accountType, String firstName,
            String lastName,
            String transactionType, String transactionDate,
            double transactionAmount, double accountBalance,
            double interestEarned) {
        super(accountNumber, accountType, firstName, lastName,
                transactionType, transactionDate, transactionAmount, accountBalance,
                interestEarned);
    }

    // Create Override Functions for processInterestEarned & processWithdrawal

    @Override
    public void processWithdrawal(double amt) {
        if (withdrawalTaken) {
            throw new IllegalArgumentException("Withdrawal already taken for this certificate");
        }
        if (amt <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (amt > accountBalance) {
            throw new IllegalArgumentException("Withdrawal amount is greater than current account balance");
        }
        accountBalance -= amt;
        setAccountBalance(accountBalance);
        withdrawalTaken = true;
    }

    @Override
    public void processWithdrawal(double amt, LocalDate currentDate) {
        reset(currentDate);
        if (withdrawalTaken) {
            throw new IllegalArgumentException("Withdrawal already taken for this certificate");
        }
        if (amt <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be greater than zero");
        }
        if (amt > accountBalance) {
            throw new IllegalArgumentException("Withdrawal amount is greater than current account balance");
        }
        accountBalance -= amt;
        setAccountBalance(accountBalance);
        withdrawalTaken = true;
        lastWithdrawalDate = currentDate;
    }

    @Override
    public void processInterestEarned(double balance, double rate) {
        double rateamount = balance * (rate * (5.0 / 365.0));
        this.interestEarned += rateamount;
        this.accountBalance += rateamount;
    }
}
