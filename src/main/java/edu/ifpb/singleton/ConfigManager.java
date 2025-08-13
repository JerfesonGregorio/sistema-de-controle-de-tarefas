package edu.ifpb.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigManager {
    private static ConfigManager instance;
    private final Connection connection;

    private ConfigManager() throws SQLException {
        try {
            String url = System.getenv().getOrDefault(
                    "DB_URL", "jdbc:postgresql://localhost:5432/postgres"
            );
            String user = System.getenv().getOrDefault(
                    "DB_USER", "postgres"
            );
            String password = System.getenv().getOrDefault(
                    "DB_PASSWORD", "postgres"
            );
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException ex) {
            throw new SQLException("Erro conectando ao banco", ex);
        }
    }

    public static ConfigManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConfigManager();
        } else if (instance.getConnection().isClosed()) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
