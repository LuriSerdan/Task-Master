package br.com.taskmaster.dao;

import br.com.taskmaster.model.Tarefa;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/taskmaster";
    private static final String USER = "root"; // Altere conforme necessário
    private static final String PASS = "";     // Altere conforme necessário

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erro ao carregar o driver JDBC: " + e.getMessage());
            throw new SQLException("Driver JDBC não encontrado.");
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }

    /**
     * Lista todas as tarefas do banco de dados.
     * @return Lista de objetos Tarefa.
     */
    public List<Tarefa> listar() {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM tarefa ORDER BY data_entrega ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Tarefa t = new Tarefa();
                t.setId(rs.getInt("id"));
                t.setNome(rs.getString("nome"));
                t.setDescricao(rs.getString("descricao"));
                t.setDataCriacao(rs.getDate("data_criacao"));
                t.setDataEntrega(rs.getDate("data_entrega"));
                t.setProjetoId(rs.getInt("projeto_id"));
                t.setResponsavelId(rs.getInt("responsavel_id"));
                t.setStatusId(rs.getInt("status_id")); // 1: Pendente, 2: Em Progresso, 3: Concluída

                tarefas.add(t);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar tarefas: " + e.getMessage());
            e.printStackTrace();
        }
        return tarefas;
    }
    
    public List<Tarefa> listarPorProjeto(int projetoId) {
        List<Tarefa> tarefas = new ArrayList<>();
        // Aqui usamos o WHERE para filtrar
        String sql = "SELECT * FROM tarefa WHERE projeto_id = ? ORDER BY data_entrega ASC";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projetoId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tarefa t = new Tarefa();
                    t.setId(rs.getInt("id"));
                    t.setNome(rs.getString("nome"));
                    t.setDescricao(rs.getString("descricao"));
                    t.setDataCriacao(rs.getDate("data_criacao"));
                    t.setDataEntrega(rs.getDate("data_entrega"));
                    t.setProjetoId(rs.getInt("projeto_id"));
                    t.setResponsavelId(rs.getInt("responsavel_id"));
                    t.setStatusId(rs.getInt("status_id"));
                    tarefas.add(t);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao listar tarefas por projeto: " + e.getMessage());
            e.printStackTrace();
        }
        return tarefas;
    }
    
    public void deletar(int id) {
        String sql = "DELETE FROM tarefa WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.execute();

        } catch (SQLException e) {
            System.err.println("Erro ao deletar tarefa: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void inserir(Tarefa tarefa) {
    String sql = "INSERT INTO tarefa (nome, descricao, data_criacao, data_entrega, projeto_id, responsavel_id, status_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, tarefa.getNome());
        stmt.setString(2, tarefa.getDescricao());

        // Conversão de Data (Java Util -> Java SQL)
        if (tarefa.getDataCriacao() == null) {
            stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
        } else {
            stmt.setDate(3, new java.sql.Date(tarefa.getDataCriacao().getTime()));
        }

        // Data de Entrega
        if (tarefa.getDataEntrega() != null) {
            stmt.setDate(4, new java.sql.Date(tarefa.getDataEntrega().getTime()));
        } else {
            stmt.setNull(4, java.sql.Types.DATE);
        }

        if (tarefa.getProjetoId() != null) stmt.setInt(5, tarefa.getProjetoId());
        else stmt.setNull(5, java.sql.Types.INTEGER);

        if (tarefa.getResponsavelId() != null) stmt.setInt(6, tarefa.getResponsavelId());
        else stmt.setNull(6, java.sql.Types.INTEGER);

        if (tarefa.getStatusId() != null) stmt.setInt(7, tarefa.getStatusId());
        else stmt.setInt(7, 1); // Default: 1 (Pendente)

        stmt.execute();

    } catch (SQLException e) {
        System.err.println("Erro ao inserir tarefa: " + e.getMessage());
        e.printStackTrace();
    }
}
}