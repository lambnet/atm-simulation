package service;

import entity.Account;
import entity.Transaction;
import repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private List<Transaction> trxHistories = new ArrayList<>();

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService) {
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public List<Transaction> getAll() {
        return transactionRepository.getAll();
    }

    public boolean addTrx(Transaction transaction) {
        return trxHistories.add(transaction);
    }

    public List<Transaction> getTrxHistories() {
        return trxHistories;
    }

    public void withdraw(int amount) {
        var account = accountService.getLoggedAcc();
        if (amount > account.getBalance()) {
            System.out.println("Insufficient balance $" + amount);
        } else {
            account.setBalance(account.getBalance() - amount);
            System.out.printf(""" 
                    Withdraw Summary
                    Date: %tF 
                    Withdraw: $%d
                    Balance: $%.2f
                    %n""", LocalDateTime.now(), amount, account.getBalance());

            var trx = new Transaction(account.getAccountNumber(), "######", Transaction.TransactionType.WITHDRAW, amount);
            addTrx(trx);
            // write to csv
            //accountService.writeToCsv(accountService.getAll());
            transactionRepository.writeTransactions(getTrxHistories());
        }
    }

    public Account findAcc(String accNumber) {
        return accountService.getAll().stream()
                .filter(a -> a.getAccountNumber().equals(accNumber))
                .findAny().orElseThrow();
    }

    public Transaction processTransfer(String destination, int amount, String reference) {
        if (!destination.matches("\\d+") || !accountService.isExist(accountService.getAll(), destination)) {
            System.out.println("Invalid Account");
            return null;
        }
        if (!reference.isEmpty() && !reference.matches("\\d+")) {
            System.out.println("Invalid Reference Number");
            return null;
        }
        try {
            var dest = findAcc(destination);
            if (amount > 1000) {
                System.out.println("Maximum amount to transfer is $1000");
                return null;
            }
            if (amount < 1) {
                System.out.println("Minimum amount to transfer is $1");
                return null;
            }
            if (accountService.getLoggedAcc().getBalance() > amount) {
                accountService.getLoggedAcc().setBalance(accountService.getLoggedAcc().getBalance() - amount);
                dest.setBalance(dest.getBalance() + amount);
            } else {
                System.out.println("Insufficient balance $" + amount);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount");
        }
        var trx = new Transaction(accountService.getLoggedAcc().getAccountNumber(), destination, Transaction.TransactionType.TRANSFER, amount);
        addTrx(trx);
        // write to csv
        // accountService.writeToCsv(accountService.getAll());
        transactionRepository.writeTransactions(getTrxHistories());
        return trx;
    }

    public List<Transaction> getTrxHistoryByAccNumber(String accNumber) {
        return trxHistories.stream()
                .filter(trx -> trx.getSenderAccountNumber().equals(accNumber))
                .sorted(Comparator.comparing(Transaction::getCreatedAt).reversed())
                .limit(10)
                .toList();
    }
}
