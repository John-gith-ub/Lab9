
import java.io.PrintWriter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String inputFile = "BankInfo.csv";
        String reportFile = "report.txt";
        String errorFile = "error.txt";

        ArrayList<BankAccount> accounts = new ArrayList<>();

        Functions.processFile(inputFile, errorFile, accounts);
        Functions.printReport(accounts, reportFile);

        try (PrintWriter reportOut = new PrintWriter(new java.io.FileWriter(reportFile, true))) {
            /// reportOut.println("Report generated in '" + reportFile + "'.");
        } catch (java.io.IOException e) {
            // report file write failed; nothing else should be printed to terminal
        }

        try (PrintWriter errorOut = new PrintWriter(new java.io.FileWriter(errorFile, true))) {
            /// errorOut.println("Errors (if any) logged in '" + errorFile + "'.");
        } catch (java.io.IOException e) {
            // error file write failed; nothing else should be printed to terminal
        }
    }
}
