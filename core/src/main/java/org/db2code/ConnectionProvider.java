package org.db2code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionProvider implements AutoCloseable {

    private final Connection connection;

    public ConnectionProvider(
            String className, String jdbcUrl, String jdbcUser, String jdbcPassword) {
        initDriver(className);
        this.connection = initConnection(jdbcUrl, jdbcUser, jdbcPassword);
    }

    public Connection getConnection() {
        return connection;
    }

    private Connection initConnection(String jdbcUrl, String jdbcUser, String jdbcPassword) {
        try {
            return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void initDriver(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
