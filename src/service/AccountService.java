package service;

import entity.Account;
import repository.AccountRepository;

import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;
    private Account loggedAcc;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> getAll(){
        return accountRepository.readAccounts();
    }

    public void writeToCsv(List<Account> accounts){
        accountRepository.writeAccounts(accounts);
    }

    public Account validateLogin(List<Account> accounts, String accNum, String pin){
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
        if (isExist(accounts, accNum, pin)) {
            var account = findAcc(accounts,accNum,pin);
            setLoggedAcc(account);
            return account;
        } else {
            System.out.println("Invalid Account Number/PIN");
            return null;
        }
    }

    public Account getLoggedAcc() {
        return loggedAcc;
    }

    public void setLoggedAcc(Account account){
        this.loggedAcc = account;
    }

    public boolean isExist(List<Account> accounts, String accNum, String pin) {
        return accounts.stream().anyMatch(
                acc -> acc.getAccountNumber().equals(accNum) && acc.getPin().equals(pin));
    }

    public boolean isExist(List<Account> accounts, String accNum) {
        return accounts.stream().anyMatch(
                acc -> acc.getAccountNumber().equals(accNum));
    }

    public Account findAcc(List<Account> accounts, String accNumber, String pin) {
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accNumber) && a.getPin().equals(pin))
                .findAny().orElseThrow();
    }
}
