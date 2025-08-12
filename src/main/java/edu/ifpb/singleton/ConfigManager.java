package edu.ifpb.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigManager {
    private static ConfigManager instance;
    private Connection connection;

    private String url = "jdbc:postgresql://localhost:5432/seubanco";
    private String user = "seuusuario";
    private String password = "suasenha";

    private ConfigManager() throws SQLException {
        try {
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
