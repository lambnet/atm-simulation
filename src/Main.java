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
}
