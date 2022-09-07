package banking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class PreparedDataBase {
    private final String DB_URL;

    public PreparedDataBase(String[] args) {
        DB_URL = "jdbc:sqlite:" + getNameDB(args);

    }

    public void createTable() {
        try (Connection connection = createConnection();
             Statement statement = connection.createStatement()) {

            String sqlCreateTable = "CREATE TABLE IF NOT EXISTS card(\n" +
                    "id INTEGER,\n" +
                    "number TEXT,\n" +
                    "pin TEXT,\n" +
                    "balance INTEGER DEFAULT 0);";

            statement.executeUpdate(sqlCreateTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection createConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private String getNameDB(String[] args) {
        if (args.length > 1) {
            if ("-fileName".equals(args[0])) {
                return args[1];
            }
        }
        return "test";
    }
}
