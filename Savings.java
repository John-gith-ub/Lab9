public class Savings extends BankAccount {
        public Savings(int accountNumber, String accountType, String firstName,
                        String lastName,
                        String transactionType, String transactionDate,
                        double transactionAmount, double accountBalance,
                        double interestEarned) {
                super(accountNumber, accountType, firstName, lastName,
                                transactionType, transactionDate, transactionAmount, accountBalance,
                                interestEarned);
        }

        // Create Override Functions for processInterestEarned
        @Override
        public void processInterestEarned(double balance, double rate) {
                double rateamount = balance * (rate * (5.0 / 365.0));
                this.interestEarned += rateamount;
                this.accountBalance += rateamount;
        }

}
