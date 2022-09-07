package banking.model;

import java.util.Objects;

public class BankAccount {
    private int id;
    private String cardNumber;
    private String pin;
    private long balance;

    public BankAccount(int id, String cardNumber, String pin, long balance) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = balance;
    }

    public BankAccount(String cardNumber, String pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankAccount)) return false;
        BankAccount that = (BankAccount) o;
        return id == that.id && Objects.equals(cardNumber, that.cardNumber) && Objects.equals(pin, that.pin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cardNumber, pin);
    }
}
