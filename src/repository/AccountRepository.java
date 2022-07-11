package repository;

import entity.Account;
import util.CheckDuplicate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRepository {
    private final Path path;
    private static final int ACCOUNT_COLUMN_LENGTH = 4;

    public AccountRepository(Path path) {
        this.path = path;
    }

    public List<Account> readAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            accounts = reader.lines()
                    .skip(1) // skip for Header
                    .map(this::getAccount)
                    .toList();
        } catch (IOException e) {
            System.exit(0);
        }
        var duplicate = accounts
                .stream()
                .map(Account::getAccountNumber)
                .collect(Collectors.toSet());
        if(duplicate.size() < accounts.size()){
            System.out.println("There can't be 2 different accounts with the same Account Number");
            System.exit(0);
        }
        return accounts;
    }

    private Account getAccount(String line) {
        String[] fields = line.split(",");
        if (fields.length != ACCOUNT_COLUMN_LENGTH) {
            throw new RuntimeException("Invalid line in CSV file " + line);
        }
        return new Account(fields[0], fields[1], Double.parseDouble(fields[2]), fields[3]);
    }

    public void writeAccounts(List<Account> accounts){
        try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
            for(var acc : convertToString(accounts)){
                writer.write(acc);
                writer.newLine();
            }
        }catch (IOException e){
            System.exit(0);
        }
    }

    private List<String> convertToString(List<Account> accounts){
        return accounts.stream()
                .map(acc -> new String[]{acc.getName(),acc.getPin(), String.valueOf(acc.getBalance()),acc.getAccountNumber()})
                .map(a -> String.join(",",a))
                .toList();
    }
}
