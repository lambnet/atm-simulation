import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        /*
         * ATM record account
         * */
        var john = new Account("John Doe", "012108", 100, "112233");
        var jane = new Account("Jane Doe", "932012", 30, "112244");
        var accounts = new ArrayList<>(Arrays.asList(john, jane));
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== ATM Welcome Screen === ");
            System.out.println("Enter Account Number: ");
            var accNum = scanner.nextLine();
            System.out.println("Enter PIN: ");
            var pin = scanner.nextLine();

            // Validate account
            var authenticatedAcc = validateLogin(accounts, accNum, pin);
            if (!(authenticatedAcc == null)) {
                Main.transactionScreen(authenticatedAcc, accounts, scanner);
            }
        }
    }

    public static Account validateLogin(List<Account> accounts, String accNum, String pin) {
        if (accNum.length() != 6) {
            System.out.println("Account Number should have 6 digits length");
            return null;
        }
        // regex for string only contains number -> "[0-9]+" or "\\d+"
        if (!accNum.matches("\\d+")) {
            System.out.println("Account Number should only contains numbers [0-9]");
            return null;
        }
        if (pin.length() != 6) {
            System.out.println("PIN should have 6 digits length");
            return null;
        }
        if (!pin.matches("\\d+")) {
            System.out.println("PIN should only contains numbers [0-9]");
            return null;
        }
        if (Main.IsExist(accounts, accNum, pin)) {
            return accounts.stream()
                    .filter(a -> a.getAccountNumber().equals(accNum) && a.getPin().equals(pin))
                    .findAny().get();
        } else {
            System.out.println("Invalid Account Number/PIN");
            return null;
        }

    }

    public static boolean IsExist(List<Account> accounts, String accNum, String pin) {
        return accounts.stream().anyMatch(
                acc -> acc.getAccountNumber().equals(accNum) && acc.getPin().equals(pin));
    }

    public static boolean IsExist(List<Account> accounts, String accNum) {
        return accounts.stream().anyMatch(
                acc -> acc.getAccountNumber().equals(accNum));
    }

    public static void withdrawScreen(Account account,List<Account> accounts,Scanner scanner) {
        System.out.println("=== Withdraw Screen ===");
        System.out.println("" +
                "1. $10 \n" +
                "2. $50 \n" +
                "3. 100 \n" +
                "4. Other \n" +
                "5. Back \n" +
                "Please choose option[5]: ");
        String input = scanner.nextLine();
        int amount = 0;
        switch (input) {
            case "1" -> amount = 10;
            case "2" -> amount = 50;
            case "3" -> amount = 100;
            case "4" -> {
                try {
                    System.out.println("Other withdraw amount to withdraw: ");
                    amount = Integer.parseInt(scanner.nextLine());
                    if (amount > 1000) {
                        System.out.println("Maximum amount to withdraw is $1000");
                    }
                    if (amount % 10 != 0) {
                        System.out.println("Invalid amount");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid amount");
                } finally {
                    scanner.nextLine();
                }
            }
            default -> transactionScreen(account,accounts,scanner);
        }

        if (amount > 0) {
            if (amount > account.getBalance()) {
                System.out.println("Insufficient balance $" + amount);
            }
            account.setBalance(account.getBalance() - amount);
            System.out.println("Summary " +
                    "\nDate: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a")) +
                    "\nWithdraw: $" + amount +
                    "\nBalance: " + account.getBalance());
            transactionScreen(account,accounts,scanner);
        }
    }

    public static void transferScreen(Account account, List<Account> accounts, Scanner scanner) {
        System.out.println("=== Transfer Fund Screen ===");
        System.out.println("Please enter destination account and press enter to continue or press enter to go back to Transaction: ");
        String destination = scanner.nextLine();
        if (destination.equals("")) {
            transactionScreen(account,accounts,scanner);
        }
        System.out.println("Please enter transfer amount and press enter to continue or press enter to go back to Transaction: ");
        String amount = scanner.nextLine();
        System.out.println("Reference Number: (This is an autogenerated random 6 digits number)press enter to continue");
        String reference = scanner.nextLine();
        System.out.println("\n Transfer Confirmation" +
                "\n Destination Account: " + destination + "\n Transfer Amount: $" + amount +
                "\n Reference Number: " + reference +
                "\n ----------" +
                "\n 1.Confirm Transfer" +
                "\n 2.Cancel Transfer" +
                "\n Choose option[2]; ");
        String option = scanner.nextLine();
        switch (option) {
            case "1" -> processTransfer(account, destination, accounts, amount, reference, scanner);
            case "2" -> account = null;
        }
    }

    public static Account findAcc(List<Account> accounts, String accNumber) {
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accNumber))
                .findAny().get();
    }


    public static void processTransfer(Account account, String destination, List<Account> accounts,
                                       String amount, String reference, Scanner scanner) {

        if (!destination.matches("\\d+") || !Main.IsExist(accounts, destination)) {
            System.out.println("Invalid Account");
            return;
        }
        if (!reference.isEmpty() && !reference.matches("\\d+")) {
            System.out.println("Invalid Reference Number");
            return;
        }
        try {
            var dest = findAcc(accounts, destination);
            if (Integer.parseInt(amount) > 1000) {
                System.out.println("Maximum amount to transfer is $1000");
                return;
            }
            if (Integer.parseInt(amount) < 1) {
                System.out.println("Minimum amount to transfer is $1");
                return;
            }
            if (account.getBalance() > Integer.parseInt(amount) ) {
                account.setBalance(account.getBalance() - Integer.parseInt(amount));
                dest.setBalance(dest.getBalance() + Integer.parseInt(amount));
                transferSummaryScreen(account, accounts,destination, amount, reference, scanner);
            }
            System.out.println("Insufficient balance $" + amount);
            transactionScreen(account, accounts,scanner);
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount");
        }
    }

    public static void transferSummaryScreen(Account account, List<Account> accounts,String destination, String amount, String ref, Scanner scanner) {
        System.out.println("Fund transfer Summary");
        System.out.println("Destination Account: " + destination +
                "\n Transfer amount: " + amount +
                "\n Reference Number: " + ref +
                "\n Balance : " + account.getBalance());
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.println("Choose Option[2]");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> transactionScreen(account, accounts, scanner);
            case "default" -> account = null;
        }
    }

    public static void transactionScreen(Account account, List<Account> accounts, Scanner scanner) {
        System.out.println("=== Transaction Screen ===");
        System.out.println("" +
                "1. Withdraw \n" +
                "2. Fund Transfer \n" +
                "3. Exit \n" +
                "Please choose option[3]: ");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> withdrawScreen(account, accounts,scanner);
            case "2" -> transferScreen(account, accounts, scanner);
            default -> account = null;
        }
    }
}
