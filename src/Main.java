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
        var accounts = new ArrayList<>(Arrays.asList(john,jane));
        Scanner scanner = new Scanner(System.in);

        while(true){
            System.out.println("=== ATM Welcome Screen === ");
            System.out.println("Enter Account Number: ");
            var accNum = scanner.nextLine();
            System.out.println("Enter PIN: ");
            var pin = scanner.nextLine();

            // Validate account
            var authenticatedAcc = validateLogin(accounts,accNum,pin);
            if(!(authenticatedAcc == null)){
                System.out.println("=== Transaction Screen ===");
                System.out.println("" +
                        "1. Withdraw \n" +
                        "2. Fund Transfer \n" +
                        "3. Exit \n" +
                        "Please choose option[3]: ");
                String input = scanner.nextLine();
                switch (input){
                    case "1" -> withdrawScreen(authenticatedAcc,scanner);
                    case "2" -> System.out.println("dasda");
                    default -> authenticatedAcc=null;
                }

            }

        }

    }
    public static Account validateLogin(List<Account> accounts ,String accNum, String pin){
        if(accNum.length() != 6) {
            System.out.println("Account Number should have 6 digits length");
            return null;
        }
        // regex for string only contains number -> "[0-9]+" or "\\d+"
        if(!accNum.matches("\\d+")){
            System.out.println("Account Number should only contains numbers [0-9]");
            return null;
        }
        if(pin.length() != 6){
            System.out.println("PIN should have 6 digits length");
            return null;
        }
        if(!pin.matches("\\d+")){
            System.out.println("PIN should only contains numbers [0-9]");
            return null;
        }
        if(Main.IsExist(accounts,accNum,pin)){
            return accounts.stream()
                    .filter(a -> a.getAccountNumber().equals(accNum) && a.getPin().equals(pin))
                    .findAny().get();
        }else {
            System.out.println("Invalid Account Number/PIN");
            return null;
        }

    }

    public static boolean IsExist(List<Account> accounts,String accNum, String pin){
        return accounts.stream().anyMatch(
                acc -> acc.getAccountNumber().equals(accNum) && acc.getPin().equals(pin));
    }

    public static void withdrawScreen(Account account, Scanner scanner){
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
        switch (input){
            case "1" -> amount = 10;
            case "2" -> amount = 50;
            case "3" -> amount = 100;
            case "4" -> {
                try{
                    System.out.println("Other withdraw amount to withdraw: ");
                    amount = Integer.parseInt(scanner.nextLine());
                    if(amount>1000){
                        System.out.println("Maximum amount to withdraw is $1000");
                    }
                    if(amount % 10 != 0){
                        System.out.println("Invalid amount");
                    }
                }catch (NumberFormatException e){
                    System.out.println("Invalid amount");
                }finally {
                    scanner.nextLine();
                }
            }
            default -> withdrawScreen(account,scanner); //TODO go back to Transaction screen instead of back to login
        }

        if(amount > 0){
            if(amount > account.getBalance()){
                System.out.println("Insufficient balance $" + amount);
            }
            account.setBalance(account.getBalance()-amount);
            DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a");
            System.out.println("Summary \n" +
                    "Date: "+ LocalDateTime.now().format(dateTime) +
                    "\nWithdraw: $" + amount +
                    "\nBalance: " + account.getBalance());
        }
    }

}
