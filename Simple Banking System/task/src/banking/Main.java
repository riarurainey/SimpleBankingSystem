package banking;

import banking.dao.BankAccountDao;
import banking.dao.BankAccountDaoImpl;
import banking.dao.PreparedDataBase;
import banking.service.BankAccountService;

public class Main {
    public static void main(String[] args) {
        PreparedDataBase preparedDataBase = new PreparedDataBase(args);
        preparedDataBase.createTable();
        BankAccountDao bankAccountDao = new BankAccountDaoImpl(preparedDataBase);
        BankAccountService bankAccountService = new BankAccountService(bankAccountDao);
        bankAccountService.mainMenu();

    }
}