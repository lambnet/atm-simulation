package repository;

import entity.Account;
import util.CheckDuplicate;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRepository {
    private final Path path;

    public AccountRepository(Path path) {
        this.path = path;
    }

    public List<Account> readAccounts() {
        List<Account> accounts = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            accounts = reader.lines()
                    .skip(1)
                    .map(this::getAccount)
                    .toList();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
        var duplicate = CheckDuplicate.duplicateList(accounts)
                .stream()
                .map(Account::getAccountNumber)
                .collect(Collectors.toSet());
        System.out.println(duplicate);
        if(duplicate.size()>0){
            System.out.println("There can't be 2 different accounts with the same Account Number");
            System.exit(0);
        }
        return accounts;
    }

    private Account getAccount(String line) {
        String[] fields = line.split(",");
        if (fields.length != 4) {
            throw new RuntimeException("Invalid line in CSV file " + line);
        }
        return new Account(fields[0], fields[1], Double.parseDouble(fields[2]), fields[3]);
    }




}
