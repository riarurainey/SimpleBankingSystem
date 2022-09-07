package banking.dao;

import banking.model.BankAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDaoImpl implements BankAccountDao {

    private final PreparedDataBase preparedDataBase;

    public BankAccountDaoImpl(PreparedDataBase preparedDataBase) {
        this.preparedDataBase = preparedDataBase;
    }

    @Override
    public void createAccount(String cardNumber, String pin) {

        try (Connection connection = preparedDataBase.createConnection();
             PreparedStatement prepareStatement = createPreparedStatementForInsert(connection, cardNumber, pin)) {
            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public long getAccountBalance(String cardNumber, String pin) {
        long balance = 0;

        try (Connection connection = preparedDataBase.createConnection();
             PreparedStatement prepareStatement = createPreparedStatementForGetBalance(connection, cardNumber, pin)) {
            ResultSet set = prepareStatement.executeQuery();
            balance = set.getLong(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    @Override
    public void addIncome(String cardNumber, String pin, long income) {

        try (Connection connection = preparedDataBase.createConnection();
             PreparedStatement prepareStatement = createPreparedStatementForUpdateBalance(connection, cardNumber, income)) {
            prepareStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void doTransfer(String cardNumberFrom, String cardNumberTo, long amount) {

        try (Connection connection = preparedDataBase.createConnection();
             PreparedStatement prepareStatementFromCard = createPreparedStatementForUpdateBalance(connection, cardNumberFrom, -amount);
             PreparedStatement prepareStatementToCard = createPreparedStatementForUpdateBalance(connection, cardNumberTo, amount);) {
            prepareStatementFromCard.executeUpdate();
            prepareStatementToCard.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void closeAccount(String cardNumber) {
        try (Connection connection = preparedDataBase.createConnection();
             PreparedStatement prepareStatement = createPreparedStatementForDeleteAccount(connection, cardNumber)) {
            prepareStatement.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BankAccount> synchronizedListWithDb() {
        List<BankAccount> list = new ArrayList<>();

        try (Connection connection = preparedDataBase.createConnection();
             PreparedStatement prepareStatement = createPreparedStatementForSynchronizedListWithDb(connection)) {

            ResultSet set = prepareStatement.executeQuery();
            while (set.next()) {
                int id = set.getInt(1);
                String cardNumber = set.getString(2);
                String pin = set.getString(3);
                long balance = set.getLong(4);
                list.add(new BankAccount(id, cardNumber, pin, balance));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private PreparedStatement createPreparedStatementForSynchronizedListWithDb(Connection connection)
            throws SQLException {
        String selectAll = "SELECT * FROM card";
        PreparedStatement ps = connection.prepareStatement(selectAll);
        return ps;
    }

    private PreparedStatement createPreparedStatementForDeleteAccount(Connection connection, String cardNumber)
            throws SQLException {
        String deleteAccount = "DELETE FROM card WHERE number = ?";
        PreparedStatement ps = connection.prepareStatement(deleteAccount);
        ps.setString(1, cardNumber);


        return ps;
    }

    private PreparedStatement createPreparedStatementForInsert(Connection connection, String cardNumber, String pin)
            throws SQLException {
        String insertNewCard = "INSERT INTO card(number, pin) VALUES (? , ?)";
        PreparedStatement ps = connection.prepareStatement(insertNewCard);
        ps.setString(1, cardNumber);
        ps.setString(2, pin);
        return ps;
    }

    private PreparedStatement createPreparedStatementForGetBalance(Connection connection, String cardNumber, String pin)
            throws SQLException {
        String getBalance = "SELECT balance FROM card WHERE number = ? AND pin = ?";
        PreparedStatement ps = connection.prepareStatement(getBalance);
        ps.setString(1, cardNumber);
        ps.setString(2, pin);
        return ps;
    }


    private PreparedStatement createPreparedStatementForUpdateBalance(Connection connection, String cardNumber,
                                                                      long income) throws SQLException {
        String updateBalance = "UPDATE card SET balance = balance + ? WHERE number = ?";
        PreparedStatement ps = connection.prepareStatement(updateBalance);
        ps.setLong(1, income);
        ps.setString(2, cardNumber);

        return ps;
    }
}


