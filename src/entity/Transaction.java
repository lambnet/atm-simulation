package entity;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    private enum TransactionType{
        WITHDRAW, TRANSFER
    }
    private UUID id;
    private String accountNumber;
    private TransactionType transactionType;
    private double amount;
    private OffsetDateTime createdAt;

    public Transaction(String accountNumber, TransactionType transactionType, double amount) {
        this.id = UUID.randomUUID();
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
