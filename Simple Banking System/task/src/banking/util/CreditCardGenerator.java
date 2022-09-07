package banking.util;

import java.util.Random;

public class CreditCardGenerator {

    private final static String BIN = "400000";
    static Random random = new Random();

    public static String generateCard() {
        StringBuilder cardNumber = new StringBuilder(BIN);
        for (int i = 0; i < 9; i++) {
            int digit = random.nextInt(10);
            cardNumber.append(digit);
        }

        int checkDigit = getCheckDigit(cardNumber.toString());
        return String.format("%s%d", cardNumber, checkDigit);
    }

    public static String generatePin() {
        int num = random.nextInt(10000);
        return String.format("%04d", num);
    }

    private static int getCheckDigit(String cardNumber) {
        int sum = 0;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(String.valueOf(cardNumber.charAt(i)));
            if (i % 2 == 0) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit / 10) + (digit % 10);
                }
            }
            sum += digit;
        }

        int mod = sum % 10;
        return (10 - mod) % 10;

    }

    public static boolean check (String cardNumber) {
        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(cardNumber.substring(i, i + 1));
            if (alternate) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            sum += n;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }
}