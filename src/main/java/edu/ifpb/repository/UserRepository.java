package edu.ifpb.repository;

import edu.ifpb.observer.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    // Padrão: Repository
    // Onde aplicado: toda a classe UserRepository
    // Como implementado: encapsula o acesso a dados da entidade User, fornecendo métodos para salvar, buscar, deletar e listar usuários.
    // Por que foi escolhido: centraliza a lógica de persistência, facilitando manutenção e testes, e desacopla a aplicação da camada de persistência.
    private final Connection connection;

    public UserRepository(Connection connection) {
        this.connection = connection;
    }

    public void save(User user) throws SQLException {
        // Padrão: Data Mapper / Repository
        // Onde aplicado: método save
        // Como implementado: mapeia o objeto User para a tabela users no banco de dados e persiste seus dados usando PreparedStatement.
        // Por que foi escolhido: evita SQL inline espalhado pela aplicação e mantém a consistência da persistência de dados.
        String sql = "INSERT INTO users (name) VALUES (?) ON CONFLICT (name) DO NOTHING";

        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getName());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1);
                user.setId(generatedId);
            }
        }
    }

    public User findByName(String name) throws RuntimeException, SQLException {
        // Padrão: Repository / Data Mapper
        // Onde aplicado: método findByName
        // Como implementado: busca um usuário pelo nome usando PreparedStatement, convertendo o resultado em um objeto User.
        // Por que foi escolhido: mantém a lógica de acesso a dados isolada, evitando que a aplicação manipule ResultSet diretamente.
        String formatName = name.trim().toLowerCase();
        String sql = "SELECT id, name FROM users WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, formatName);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User user = new User(rs.getString("name"));
                user.setId(rs.getInt("id"));
                return user;
            }
        }
        return null;
    }

    public boolean deleteByName(String name) throws SQLException {
        // Padrão: Repository
        // Onde aplicado: método deleteByName
        // Como implementado: executa um DELETE no banco de dados baseado no nome do usuário e retorna true se algum registro foi removido.
        // Por que foi escolhido: padroniza operações de remoção e centraliza regras de negócio relacionadas à exclusão de usuários.
        String formatName = name.trim().toLowerCase(); // manter o mesmo padrão
        String sql = "DELETE FROM users WHERE name = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, formatName);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }


    public List<User> findAll() throws SQLException {
        // Padrão: Repository / Data Mapper
        // Onde aplicado: método findAll
        // Como implementado: executa um SELECT para buscar todos os usuários, mapeando cada linha para um objeto User e retornando uma lista.
        // Por que foi escolhido: fornece uma forma consistente de recuperar múltiplos objetos da tabela users, mantendo o acesso a dados isolado.
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name FROM users";
        try (Statement stmt = connection.createStatement()) {

            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                User user = new User(rs.getString("name"));
                user.setId(rs.getInt("id"));
                users.add(user);
            }
        }
        return users;
    }

}
