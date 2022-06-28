package repository;

import entity.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class TransactionRepository {
    private final Path path;

    public TransactionRepository(Path path){
        this.path = path;
    }

    public List<Transaction> readTransactions(){
        List<Transaction> transactions = new LinkedList<>();
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            transactions = reader.lines()
                    .skip(1)
                    .map(this::getTransaction)
                    .toList();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
        return transactions;
    }

    private Transaction getTransaction(String line){
        String[] fields = line.split(",");
        if(fields.length != 5){
            throw new RuntimeException("Invalid line in csv file " + line);
        }
        return new Transaction(fields[1], Transaction.TransactionType.valueOf(fields[2]),Double.parseDouble(fields[3]));
    }
}
