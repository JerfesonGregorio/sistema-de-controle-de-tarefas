package edu.ifpb.singleton;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.stream.Collectors;

public class ConfigManager {
    // 🔵 Padrão Singleton aplicado
    // Mantém apenas uma instância da classe em toda a aplicação
    private static ConfigManager instance;

    // A conexão com o banco de dados também é única
    private static Connection connection = null;

    // 🔒 Construtor privado -> impede que outras classes criem instâncias diretamente
    private ConfigManager() throws SQLException {
        try {
            // Carregamento do driver JDBC do PostgreSQL
            Class.forName("org.postgresql.Driver");

            // Configurações vindas de variáveis de ambiente
            String url = System.getenv().getOrDefault(
                    "DB_URL", "jdbc:postgresql://localhost:5432/postgres"
            );
            String user = System.getenv().getOrDefault(
                    "DB_USER", "postgres"
            );
            String password = System.getenv().getOrDefault(
                    "DB_PASSWORD", "postgres"
            );

            // Criação da conexão única com o banco
            connection = DriverManager.getConnection(url, user, password);

        } catch (SQLException ex) {
            throw new SQLException("Erro conectando ao banco", ex);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 🔵 Método Singleton: garante que só exista uma instância do ConfigManager
     * - Se não existir, cria.
     * - Se existir mas a conexão estiver fechada, recria.
     * - Caso contrário, retorna a mesma instância.
     */
    public static ConfigManager getInstance() throws SQLException {
        if (instance == null) {
            instance = new ConfigManager();
            inicializarBanco(); // Garante que o banco seja inicializado (infraestrutura pronta)
        } else if (instance.getConnection().isClosed()) {
            instance = new ConfigManager();
        }
        return instance;
    }

    // Retorna a conexão única
    public Connection getConnection() {
        return connection;
    }

    /**
     * Executa o script SQL de criação das tabelas a partir do arquivo schema.sql
     * 🎯 Motivação: Automatiza a inicialização do banco na primeira execução
     */
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

    /**
     * Verifica se o banco já possui as tabelas essenciais.
     * Caso contrário, roda o script de inicialização.
     * 🎯 Motivação: Garante que a aplicação sempre tenha a estrutura mínima
     */
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
