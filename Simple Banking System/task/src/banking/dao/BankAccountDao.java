package banking.dao;

import banking.model.BankAccount;

import java.util.List;

public interface BankAccountDao {
    void createAccount(String cardNumber, String pin);
    long getAccountBalance(String cardNumber, String pin);
    void addIncome(String cardNumber, String pin, long income);

    void doTransfer(String cardNumberFrom, String cardNumberTo, long amount);

    void closeAccount(String cardNumber);

    List<BankAccount> synchronizedListWithDb();

}
