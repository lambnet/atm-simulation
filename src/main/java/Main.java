import controller.ScreenController;
import repository.AccountRepository;
import repository.TransactionRepository;
import service.AccountService;
import service.TransactionService;

import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No CSV file found");
            return;
        }
        var accountService = new AccountService(new AccountRepository(Path.of(args[0])));
        var transactionService = new TransactionService(new TransactionRepository(Path.of(args[1])), accountService);
        var screenController = new ScreenController(accountService, transactionService);
        while (true) {
            screenController.welcomeScreen();
            break;
        }
    }
}
