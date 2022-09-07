package banking.service;

import banking.util.CreditCardGenerator;
import banking.dao.BankAccountDao;
import banking.model.BankAccount;

import java.util.List;
import java.util.Scanner;

public class BankAccountService {
    private final BankAccountDao bankAccountDao;
    private final List<BankAccount> bankAccountList;


    public BankAccountService(BankAccountDao bankAccountDao) {
        this.bankAccountDao = bankAccountDao;
        bankAccountList = synchronizedListWithDb();

    }

    public void mainMenu() {
        while (true) {
            System.out.println("1. Create an account\n" +
                    "2. Log into account\n" +
                    "0. Exit");
            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();

            switch (choice) {
                case 1: {
                    createAccount();
                    break;
                }
                case 2: {
                    loginInAccount();
                    break;
                }
                case 0: {
                    System.out.println("Bye!");
                    System.exit(0);
                }
            }
        }
    }

    private void accountMenu(String number, String pin) {
        while (true) {
            System.out.println("1. Balance\n" +
                    "2. Add income\n" +
                    "3. Do transfer\n" +
                    "4. Close account\n" +
                    "5. Log out\n" +
                    "0. Exit");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            switch (choice) {
                case 1: {

                    getBalance(number, pin);
                    break;
                }
                case 2: {
                    addIncome(number, pin);
                    break;
                }
                case 3: {
                    doTransfer(number, pin);
                    break;
                }
                case 4: {
                    closeAccount(number, pin);
                    mainMenu();
                    break;
                }
                case 5: {
                    System.out.println("You have successfully logged out!");
                    mainMenu();
                    break;
                }
                case 0: {
                    System.out.println("Bye!");
                    System.exit(0);
                }
            }
        }
    }

    private void createAccount() {
        String cardNumber = CreditCardGenerator.generateCard();
        String cardPin = CreditCardGenerator.generatePin();
        bankAccountDao.createAccount(cardNumber, cardPin);

        System.out.println("Your card has been created");
        System.out.println("Your card number:\n" + cardNumber);
        System.out.println("Your card PIN:\n" + cardPin);
        bankAccountList.add(new BankAccount(cardNumber, cardPin));
    }

    private void loginInAccount() {
        System.out.println("Enter your card number:");
        Scanner scanner = new Scanner(System.in);
        String cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        String pin = scanner.nextLine();

        BankAccount bankAccount = new BankAccount(cardNumber, pin);
        if (bankAccountList.contains(bankAccount)) {
            System.out.println("You have successfully logged in!");
            accountMenu(cardNumber, pin);
        } else {
            System.out.println("Wrong card number or PIN!");
            mainMenu();
        }
    }

    private void closeAccount(String cardNumber, String pin) {
        bankAccountDao.closeAccount(cardNumber);
        BankAccount bankAccount = new BankAccount(cardNumber, pin);
        bankAccountList.remove(bankAccount);
        System.out.println("The account has been closed!");
    }

    private void doTransfer(String cardNumberFrom, String pin) {
        System.out.println("Enter card number:");
        Scanner scanner = new Scanner(System.in);
        String cardNumberTo = scanner.nextLine();

        if (!checkValidCard(cardNumberFrom, cardNumberTo)) {
            accountMenu(cardNumberFrom, pin);
        }
        System.out.println("Enter how much money you want to transfer:");
        long amount = scanner.nextLong();
        BankAccount bankAccountFrom = bankAccountList.get(getIndex(cardNumberFrom));

        if (!checkEnoughMoney(bankAccountFrom, amount)) {
            accountMenu(cardNumberFrom, pin);
        }

        bankAccountFrom.setBalance(bankAccountFrom.getBalance() - amount);
        BankAccount bankAccountTo = bankAccountList.get(getIndex(cardNumberTo));
        bankAccountTo.setBalance(bankAccountTo.getBalance() + amount);
        bankAccountDao.doTransfer(cardNumberFrom, cardNumberTo, amount);
        System.out.println("Success!");
    }

    private void addIncome(String cardNumber, String pin) {
        System.out.println("Enter income:");
        Scanner scanner = new Scanner(System.in);
        long income = scanner.nextLong();
        bankAccountDao.addIncome(cardNumber, pin, income);
        BankAccount bankAccount = new BankAccount(cardNumber, pin);
        int index = bankAccountList.indexOf(bankAccount);
        bankAccountList.get(index).setBalance(bankAccount.getBalance() + income);
        System.out.println("Income was added!");

    }

    private void getBalance(String cardNumber, String pin) {
        long balance = bankAccountDao.getAccountBalance(cardNumber, pin);
        System.out.println("Balance: " + balance);
    }

    private int getIndex(String cardNumber) {
        for (BankAccount bankAccount : bankAccountList) {
            if (bankAccount.getCardNumber().equals(cardNumber)) {
                return bankAccountList.indexOf(bankAccount);
            }
        }
        return -1;
    }

    private boolean checkEnoughMoney(BankAccount bankAccountFrom, long amount) {
        if (bankAccountFrom.getBalance() < amount) {
            System.out.println("Not enough money!");
            return false;
        }
        return true;
    }

    private boolean checkValidCard(String cardNumberFrom, String cardNumberTo) {
        if (cardNumberTo.equals(cardNumberFrom)) {
            System.out.println("You can't transfer money to the same account!");
            return false;
        }

        if (!CreditCardGenerator.check(cardNumberTo)) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
            return false;
        }

        if (getIndex(cardNumberTo) == -1) {
            System.out.println("Such a card does not exist");
            return false;
        }
        return true;
    }

    private List<BankAccount> synchronizedListWithDb() {
        return bankAccountDao.synchronizedListWithDb();
    }
}
