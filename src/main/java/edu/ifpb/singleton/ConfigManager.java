package edu.ifpb.singleton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.stream.Collectors;

public class ConfigManager {
    private static ConfigManager instance;
    private static Connection connection = null;

    private ConfigManager() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
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
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConfigManager();
            inicializarBanco();
        } else if (instance.getConnection().isClosed()) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private static void runSchemaScript() {
        try {
            InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream("schema.sql");
            if (inputStream == null) {
                System.err.println("Arquivo schema.sql não encontrado em resources!");
                return;
            }

            String sql = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            try (Statement stmt = connection.createStatement()) {
                stmt.execute(sql);
                System.out.println("Script SQL executado com sucesso.");
            }

        } catch (Exception e) {
            System.err.println("Erro ao executar script SQL: " + e.getMessage());
        }
    }

    private static void inicializarBanco() {
        try {
            DatabaseMetaData meta = connection.getMetaData();
            ResultSet rs = meta.getTables(null, null, "users", null);

            if (!rs.next()) {
                System.out.println("Inicializando banco de dados...");
                runSchemaScript();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
