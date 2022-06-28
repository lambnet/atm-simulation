import repository.AccountRepository;
import repository.TransactionRepository;
import service.AccountService;
import service.TransactionService;

import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import static java.util.Objects.isNull;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static AccountService accountService;
    private static TransactionService transactionService;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No CSV file found");
            return;
        }
        accountService = new AccountService(new AccountRepository(Path.of(args[0])));
        transactionService = new TransactionService(new TransactionRepository(Path.of(args[1])), accountService);
        var accounts = accountService.getAll();
        var trxHistories = new ArrayList<>(transactionService.getAll());


        while (true) {
            welcomeScreen();
            System.out.println("=== ATM Welcome Screen === ");
            System.out.println("Enter entity.Account Number: ");
            var accNum = scanner.nextLine();
            System.out.println("Enter PIN: ");
            var pin = scanner.nextLine();

            // Validate account
//            var authenticatedAcc = accountService.validateLogin(accounts, accNum, pin);
//            if (!(authenticatedAcc == null)) {
//                System.out.println("=== Transaction Screen ===");
//                System.out.println("" +
//                        "1. Withdraw \n" +
//                        "2. Fund Transfer \n" +
//                        "3. Transaction History \n" +
//                        "4. Exit \n" +
//                        "Please choose option[4]: ");
//                String input = scanner.nextLine();
//                switch (input) {
//                    case "1" -> withdrawScreen();
//                    case "2" -> transferScreen();
//                    case "3" -> transactionHistoryList();
//                    default -> authenticatedAcc = null;
//                }
//            }
        }
    }

    public static void welcomeScreen() {
        System.out.println("=== ATM Welcome Screen === ");
        System.out.println("Enter entity.Account Number: ");
        var accNum = scanner.nextLine();
        System.out.println("Enter PIN: ");
        var pin = scanner.nextLine();
        var authenticatedAcc = accountService.validateLogin(accountService.getAll(), accNum, pin);
        if (!(isNull(authenticatedAcc))) {
            transactionScreen();
        }
    }

    public static void transactionScreen() {
        System.out.println("=== Transaction Screen ===");
        System.out.println("" +
                "1. Withdraw \n" +
                "2. Fund Transfer \n" +
                "3. Transaction History \n" +
                "4. Exit \n" +
                "Please choose option[4]: ");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> withdrawScreen();
            case "2" -> transferScreen();
            case "3" -> transactionHistoryScreen();
            default -> welcomeScreen();
        }
    }

    public static void withdrawScreen() {
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
            default -> transactionScreen();
        }
        if (amount > 0) {
            transactionService.withdraw(amount);
            transactionScreen();
        }
    }

    public static void transferScreen(){
        System.out.println("=== Transfer Fund Screen ===");
        System.out.println("Please enter destination account and press enter to continue or press enter to go back to Transaction: ");
        String destination = scanner.nextLine();
        if (destination.equals("")) {
            transactionScreen();
        }
        System.out.println("Please enter transfer amount and press enter to continue or press enter to go back to Transaction: ");
        String amount = scanner.nextLine();
        System.out.println("Reference Number: (This is an autogenerated random 6 digits number) press enter to continue");
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
            case "1" -> {
                if(!isNull(transactionService.processTransfer(destination,Integer.parseInt(amount), reference))){
                    transferSummaryScreen(destination,amount,reference);
                }
            }
            default -> {
                transactionScreen();
            }
        }
    }

    public static void transferSummaryScreen(String destination, String amount, String reference){
        System.out.println("Fund transfer Summary");
        System.out.println("Destination Account: " + destination +
                "\n Transfer amount: " + amount +
                "\n Reference Number: " + reference +
                "\n Balance : " + accountService.getLoggedAcc().getBalance());
        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.println("Choose Option[2]");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> transactionScreen();
            case "default" -> welcomeScreen();
        }
    }

    public static void transactionHistoryScreen(){
        var accTrx = transactionService.getAll().stream()
                .filter(trx -> trx.getAccountNumber().equals(accountService.getLoggedAcc().getAccountNumber()))
                .limit(10)
                .toList();
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm a");
        System.out.println("Transaction Type || Account Number || Amount || Created At");
        for(var trx : accTrx){
            System.out.println(trx.getTransactionType()+"||"+trx.getAccountNumber()+"||"+trx.getAmount()+"||"+trx.getCreatedAt().format(dateFormat));
        }
    }

}
