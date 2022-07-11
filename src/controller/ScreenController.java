package controller;

import service.AccountService;
import service.TransactionService;

import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Scanner;

import static java.util.Objects.isNull;

public class ScreenController {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountService accountService;
    private final TransactionService transactionService;
    private static final int MAX_BOUND = 900000;
    private static final int MIN_BOUND = 100000;
    public ScreenController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    public void welcomeScreen() {
        System.out.println("=== ATM Welcome Screen === ");
        System.out.println("Enter Account Number: ");
        var accNum = scanner.nextLine();
        System.out.println("Enter PIN: ");
        var pin = scanner.nextLine();
        var authenticatedAcc = accountService.validateLogin(accountService.getAll(), accNum, pin);
        if (!(isNull(authenticatedAcc))) {
            transactionScreen();
        } else {
            welcomeScreen();
        }
    }

    public void transactionScreen() {
        System.out.println("=== Transaction Screen ===");
        System.out.println("""
                1. Withdraw
                2. Fund Transfer
                3. Transaction History
                4. Exit
                Please choose option[4]:
                """);
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> withdrawScreen();
            case "2" -> transferScreen();
            case "3" -> transactionHistoryScreen();
            default -> welcomeScreen();
        }
    }

    public void withdrawScreen() {
        System.out.println("=== Withdraw Screen ===");
        System.out.println("""
                1. $10
                2. $50
                3. 100
                4. Other
                5. Back
                Please choose option[5]:""");
        String input = scanner.nextLine();
        switch (input) {
            case "1" -> transactionService.withdraw(10);
            case "2" -> transactionService.withdraw(50);
            case "3" -> transactionService.withdraw(100);
            case "4" -> transactionService.withdraw(otherWithdrawScreen());
            default -> transactionScreen();
        }
        transactionScreen();
    }

    private int otherWithdrawScreen() {
        int amount = 0;
        try {
            System.out.println("Other withdraw amount to withdraw: ");
            amount = Integer.parseInt(scanner.nextLine());
            if (amount > 1000) {
                System.out.println("Maximum amount to withdraw is $1000");
                withdrawScreen();
            }
            if (amount % 10 != 0) {
                System.out.println("Invalid amount");
                withdrawScreen();
            }
            return amount;
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount");
        }
        return amount;
    }

    public void transferScreen() {
        System.out.println("=== Transfer Fund Screen ===");
        System.out.println("Please enter destination account and press enter to continue or press enter to go back to Transaction: ");
        String destination = scanner.nextLine();
        if (destination.equals("")) {
            transactionScreen();
        }
        System.out.println("Please enter transfer amount and press enter to continue or press enter to go back to Transaction: ");
        String amount = scanner.nextLine();
        System.out.println("Reference Number: (This is an autogenerated random 6 digits number) press enter to continue");

        var reference = generateReference();
        System.out.printf("""
                Transfer Confirmation
                Destination Account: %s
                Transfer Amount: %s
                Reference number: %s
                ----------
                1.Confirm Transfer
                2.Cancel Transfer
                Choose Option[2]:
                %n""", destination, amount, reference);
        String option = scanner.nextLine();
        if (option.equals("1")) {
            if (!isNull(transactionService.processTransfer(destination, Integer.parseInt(amount), reference))) {
                transferSummaryScreen(destination, amount, reference);
            } else {
                transferScreen();
            }
        } else {
            transactionScreen();
        }
    }

    public void transferSummaryScreen(String destination, String amount, String reference) {
        System.out.println("Fund transfer Summary");
        System.out.printf("""
                Destination Account: %s
                Transfer Amount: %s
                Reference number: %s
                Balance: $%.2f
                %n""", destination, amount, reference, accountService.getLoggedAcc().getBalance());

        System.out.println("1. Transaction");
        System.out.println("2. Exit");
        System.out.println("Choose Option[2]");
        String input = scanner.nextLine();
        if(input.equals("1")){
            transactionScreen();
        }else{
            welcomeScreen();
        }
    }

    public void transactionHistoryScreen() {
        var accTrx = transactionService.getTrxHistoryByAccNumber(accountService.getLoggedAcc().getAccountNumber());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.println("Transaction Type || Sender Account Number || Receiver Account Number || Amount || Created At");
        for (var trx : accTrx) {
            System.out.println(trx.getTransactionType() + "||" + trx.getSenderAccountNumber() + "||" + trx.getReceiverAccountNumber() + "||" + trx.getAmount() + "||" + trx.getCreatedAt().format(dateFormat));
        }
        transactionScreen();
    }

    private static String generateReference(){
        var rand = new Random();
        var reference = rand.nextInt(MAX_BOUND)+MIN_BOUND;
        return Integer.toString(reference);
    }
}
