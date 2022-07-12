package entity;

import java.util.Objects;

public class Account {
    private String name;
    private String pin;
    private double balance;
    private String accountNumber;

    public Account(String name, String pin, double balance, String accountNumber){
        this.name = name;
        this.pin = pin;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return pin == account.pin && Double.compare(account.balance, balance) == 0 && accountNumber == account.accountNumber && Objects.equals(name, account.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, pin, balance, accountNumber);
    }
}
