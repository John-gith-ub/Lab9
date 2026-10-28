
import java.io.*;
import java.util.*;

public class Functions {

    // PRE: accepts a string as input
    // POST: returns true if string only has digits, false otherwise
    public static boolean isDigits(String str) {
        return str.matches("\\d+");
    }

    // PRE: accepts the ArrayList of bank accoutns & an account number
    // POST: returns true if the account exists, false otherwise
    public static boolean accountExists(ArrayList<BankAccount> accounts, int acct) {
        for (BankAccount accts : accounts) {
            if (accts.getAccountNumber() == acct)
                return true;
        }
        return false;
    }

    // PRE: accepts a string and verifies that it is valid
    // POST: if string is C, S or CD return true, otherwise return false
    public static boolean validType(String t) {
        // add logic here
        if (t.equalsIgnoreCase("C") || t.equalsIgnoreCase("S") ||
                t.equalsIgnoreCase("CD")) {
            return true;
        }
        return false;

    }

    // PRE: accepts a string and verifies that it is valid
    // POST: if string is NEW, INTEREST, DEPOSIT, TRANSFER, WITHDRAWL return true,
    // otherwise return false
    public static boolean validTransType(String t) {

        if (t.equalsIgnoreCase("NEW") || t.equalsIgnoreCase("DEPOSIT") ||
                t.equalsIgnoreCase("INTEREST") || t.equalsIgnoreCase("WITHDRAWAL") ||
                t.equalsIgnoreCase("TRANSFER"))
            return true;

        return false;
    }

    // PRE: accepts a string and verifies that it is a valid date
    // POST: if string contains valid information, return 1, otherwise return -1
    // can be updated to true/false
    public static int validDate(String date) {
        try {
            String[] parts = date.split("/");
            if (parts.length != 3)
                return -1;
            int year = Integer.parseInt(parts[2]);
            int month = Integer.parseInt(parts[0]);
            int day = Integer.parseInt(parts[1]);

            if (year < 1900 || year > 2024)
                return -1;
            if (month < 1 || month > 12)
                return -1;
            if (day < 1 || day > 31)
                return -1;

            return 1;
        } catch (Exception e) {
            return -1;
        }
    }

    public static void processFile(String filename, String errorLog, ArrayList<BankAccount> accounts) {
        try (
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                PrintWriter errWriter = new PrintWriter(new FileWriter(errorLog))) {
            String line;
            reader.readLine(); // skip header
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",", -1);
                try {

                    // verify the tokens ensuring that data read is valid for the transaction before
                    // processing the command
                    if (tokens.length != 7) {
                        throw new IllegalArgumentException("Wrong number of fields present");
                    }

                    String transactionType = tokens[5].trim();
                    if (!validTransType(transactionType)) {
                        throw new IllegalArgumentException("Invalid transaction type");
                    }

                    switch (transactionType.toUpperCase()) {
                        case "NEW":
                            int accountNumber = Integer.parseInt(tokens[0].trim());
                            String type = tokens[1].trim();
                            String firstName = tokens[2].trim();
                            String lastName = tokens[3].trim();
                            String date = tokens[4].trim();
                            if (!validType(type)) {
                                throw new IllegalArgumentException("Invalid account type");
                            }
                            if (validDate(date) != 1) {
                                throw new IllegalArgumentException("Invalid date");
                            }
                            if (accountNumber <= 0 || firstName.isEmpty() || lastName.isEmpty()) {
                                throw new IllegalArgumentException("Invalid new account record");
                            }
                            double newAmount = Double.parseDouble(tokens[6].trim());
                            BankAccount newAccount = findAccount(accounts, accountNumber);
                            if (newAccount == null) {
                                switch (type.toUpperCase()) {
                                    case "C":
                                        newAccount = new Checking(accountNumber, type, firstName, lastName,
                                                transactionType, date, newAmount, newAmount, 0.0);
                                        break;
                                    case "S":
                                        newAccount = new Savings(accountNumber, type, firstName, lastName,
                                                transactionType, date, newAmount, newAmount, 0.0);
                                        break;
                                    case "CD":
                                        newAccount = new Certificate(accountNumber, type, firstName, lastName,
                                                transactionType, date, newAmount, newAmount, 0.0);
                                        break;
                                    default:
                                        throw new IllegalArgumentException("Invalid account type");
                                }
                                accounts.add(newAccount);
                            }
                            break;

                        case "WITHDRAWAL":
                            int withdrawAccountNum = Integer.parseInt(tokens[0].trim());
                            if (!accountExists(accounts, withdrawAccountNum)) {
                                throw new IllegalArgumentException("Account does not exist");
                            }
                            double withdrawAmount = Double.parseDouble(tokens[6].trim());
                            if (withdrawAmount <= 0) {
                                throw new IllegalArgumentException("Withdrawal must be positive");
                            }
                            BankAccount withdrawAccount = findAccount(accounts, withdrawAccountNum);
                            withdrawAccount.processWithdrawal(withdrawAmount);
                            break;

                        case "DEPOSIT":
                            int depositAccountNum = Integer.parseInt(tokens[0].trim());
                            if (!accountExists(accounts, depositAccountNum)) {
                                throw new IllegalArgumentException("Account does not exist");
                            }
                            double depositAmount = Double.parseDouble(tokens[6].trim());
                            if (depositAmount <= 0) {
                                throw new IllegalArgumentException("Deposit must be positive");
                            }
                            BankAccount depositAccount = findAccount(accounts, depositAccountNum);
                            depositAccount.processDeposit(depositAmount);
                            break;

                        case "INTEREST":
                            if (tokens[0].trim().isEmpty()) {
                                for (BankAccount interestAccount : accounts) {
                                    interestAccount.processInterestEarned(interestAccount.getAccountBalance(), 0.05);
                                }
                            } else {
                                int interestAccountNumber = Integer.parseInt(tokens[0].trim());
                                BankAccount interestAccount = findAccount(accounts, interestAccountNumber);
                                if (interestAccount == null) {
                                    throw new IllegalArgumentException("Interest account does not exist");
                                }
                                if ("S".equalsIgnoreCase(interestAccount.getAccountType())) {
                                    if (interestAccount.getAccountBalance() > 100.00 &&
                                            interestAccount.getAccountBalance() < 999.99) {
                                        interestAccount.processInterestEarned(interestAccount.getAccountBalance(),
                                                0.05);
                                    } else if (interestAccount.getAccountBalance() >= 1000.00) {
                                        interestAccount.processInterestEarned(interestAccount.getAccountBalance(),
                                                0.075);
                                    } else {
                                        interestAccount.processInterestEarned(interestAccount.getAccountBalance(), 0.0);
                                    }
                                } else if ("C".equalsIgnoreCase(interestAccount.getAccountType())) {
                                    interestAccount.processInterestEarned(interestAccount.getAccountBalance(), 0.0);
                                } else if ("CD".equalsIgnoreCase(interestAccount.getAccountType())) {
                                    Certificate cert = (Certificate) interestAccount;
                                    if (cert.WithdrawalHasBeenTaken()) {
                                        cert.processInterestEarned(cert.getAccountBalance(), 0.025);
                                    } else {
                                        cert.processInterestEarned(cert.getAccountBalance(), 0.10);
                                    }
                                }
                            }
                            break;

                        case "TRANSFER":
                            if (tokens[0].trim().isEmpty() || tokens[1].trim().isEmpty()) {
                                throw new IllegalArgumentException(
                                        "Transfer requires source and target account numbers");
                            }
                            int sourceAccount = Integer.parseInt(tokens[0].trim());
                            int targetAccount = Integer.parseInt(tokens[1].trim());
                            BankAccount source = findAccount(accounts, sourceAccount);
                            BankAccount target = findAccount(accounts, targetAccount);
                            if (source == null || target == null) {
                                throw new IllegalArgumentException("Transfer account does not exist");
                            }
                            double transferAmount = Double.parseDouble(tokens[6].trim());
                            if (transferAmount <= 0) {
                                throw new IllegalArgumentException("Transfer value must be greater than zero");
                            }
                            if (source.getAccountBalance() < transferAmount) {
                                throw new IllegalArgumentException("Insufficient funds");
                            }
                            source.processWithdrawal(transferAmount);
                            target.processDeposit(transferAmount);
                            break;

                        default:
                            throw new IllegalArgumentException("Invalid Transaction Type.");
                    }

                } catch (Exception e) {
                    errWriter.println("Parse error in record: " + line + " - " + e.getMessage());
                    errWriter.flush();
                }

            }
        } catch (IOException e) {
            try (PrintWriter errWriter = new PrintWriter(new FileWriter(errorLog, true))) {
                errWriter.println("Error reading file: " + e.getMessage());
                errWriter.flush();
            } catch (IOException writeError) {
                // no terminal output allowed
            }
        }

    }

    // PRE: none
    // POST: sorts information in accounts by account number, prints bank account
    // information in accounts to report file
    public static void printReport(ArrayList<BankAccount> accounts, String reportFile) {
        accounts.sort(Comparator.comparing(BankAccount::getAccountNumber));
        try (PrintWriter out = new PrintWriter(new FileWriter(reportFile))) {
            out.printf("%-20s%-20s%-20s%-15s%10s%15s%n", "Account#", "Type", "First Name", "Last Name", "Balance",
                    "Interest");
            for (BankAccount acct : accounts) {
                acct.printDetails(out);
            }
        } catch (IOException e) {
            try (PrintWriter errWriter = new PrintWriter(new FileWriter("error.txt", true))) {
                errWriter.println("Error writing report: " + e.getMessage());
                errWriter.flush();
            } catch (IOException ignored) {
                // no terminal output allowed
            }
        }
    }

    public static BankAccount findAccount(ArrayList<BankAccount> accounts, int accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                return account;
            }
        }
        return null;
    }
}
