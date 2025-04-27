package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connectToDatabase();
        }
        return connection;
    }

    private static void connectToDatabase() throws SQLException {
        try {
            // Use Microsoft JDBC Driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            String connectionUrl = "jdbc:sqlserver://<your-server-name>;databaseName=<your-db-name>;integratedSecurity=true;encrypt=true;trustServerCertificate=true;";
            
            connection = DriverManager.getConnection(connectionUrl);
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQL Server Driver not found.", e);
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
