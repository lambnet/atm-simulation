package entity;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    public enum TransactionType{
        WITHDRAW, TRANSFER
    }
    private UUID id;
    private String senderAccountNumber;
    private String receiverAccountNumber;
    private TransactionType transactionType;
    private double amount;
    private OffsetDateTime createdAt;

    public Transaction(String senderAccountNumber, String receiverAccountNumber, TransactionType transactionType, double amount) {
        this.id = UUID.randomUUID();
        this.senderAccountNumber = senderAccountNumber;
        this.receiverAccountNumber = receiverAccountNumber;
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

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
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
