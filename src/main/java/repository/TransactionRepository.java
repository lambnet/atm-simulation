package repository;

import entity.Transaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class TransactionRepository implements IRepository{
    private final Path path;
    private List<Transaction> transactions;
    private static final int TRANSACTION_COLUMN_LENGTH = 6;

    public TransactionRepository(Path path){
        this.path = path;
        this.transactions = readFromFile();
    }

    public List<Transaction> getAll(){
        return transactions;
    }

    public Optional<Transaction> getByAccNumber(String accNumber){
        return transactions.stream()
                .filter(trx -> trx.getSenderAccountNumber().equals(accNumber))
                .findFirst();
    }

    public List<Transaction> readFromFile(){
        List<Transaction> transactions = new LinkedList<>();
        try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)){
            transactions = reader.lines()
                    .skip(1)
                    .map(this::getTransaction)
                    .toList();
        }catch (IOException e){
            System.exit(0);
        }
        return transactions;
    }

    private Transaction getTransaction(String line){
        String[] fields = line.split(",");
        if(fields.length != TRANSACTION_COLUMN_LENGTH){
            throw new RuntimeException("Invalid line in csv file " + line);
        }
        return new Transaction(fields[1], fields[2],Transaction.TransactionType.valueOf(fields[2]),Double.parseDouble(fields[3]));
    }

    public void writeTransactions(List<Transaction> transactions){
        try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)){
            for(var trx : convertToString(transactions)){
                writer.write(trx);
                writer.newLine();
            }
        }catch (IOException e){
            e.printStackTrace();
            System.exit(0);
        }
    }

    private List<String> convertToString(List<Transaction> transactions){
        return transactions
                .stream()
                .map(trx -> new String[]{String.valueOf(trx.getId()),trx.getSenderAccountNumber(),trx.getReceiverAccountNumber(), String.valueOf(trx.getTransactionType()),
                        String.valueOf(trx.getAmount()), String.valueOf(trx.getCreatedAt())})
                .map(r -> String.join(", ",r))
                .toList();
    }
}
