import entity.Account;
import repository.AccountRepository;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        AccountRepository accountRepository;
        if (args.length == 0) {
            System.out.println("No CSV file found");
            return;
        }
        accountRepository = new AccountRepository(Path.of(args[0]));
        var accounts = accountRepository.readAccounts();
        while (true) {
            System.out.println("=== ATM Welcome Screen === ");
            System.out.println("Enter entity.Account Number: ");
            var accNum = scanner.nextLine();
            System.out.println("Enter PIN: ");
            var pin = scanner.nextLine();

            // Validate account
            var authenticatedAcc = Service.validateLogin(accounts, accNum, pin);
            if (!(authenticatedAcc == null)) {
                Service.transactionScreen(authenticatedAcc, accounts, scanner);
            }
        }
    }
}
