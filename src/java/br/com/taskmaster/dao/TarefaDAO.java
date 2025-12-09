package br.com.taskmaster.dao;

import br.com.taskmaster.factory.ConnectionFactory;
import br.com.taskmaster.model.Tarefa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TarefaDAO {

    /**
     * Lista tarefas com base nas permissões do usuário.
     */
    public List<Tarefa> listarComPermissoes(int usuarioId, String funcaoUsuario) {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql;
        
        // Se for Gerente, pode ver todas as tarefas
        if ("Gerente".equals(funcaoUsuario)) {
            sql = "SELECT * FROM tarefa ORDER BY data_entrega ASC";
        } else {
            // Usuário comum: só vê tarefas dos projetos onde tem acesso
            sql = "SELECT DISTINCT t.* FROM tarefa t " +
                  "INNER JOIN projeto p ON t.projeto_id = p.id " +
                  "LEFT JOIN projeto_usuario pu ON p.id = pu.projeto_id " +
                  "WHERE p.lider_id = ? OR pu.usuario_id = ? " +
                  "ORDER BY t.data_entrega ASC";
        }

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (!"Gerente".equals(funcaoUsuario)) {
                stmt.setInt(1, usuarioId);
                stmt.setInt(2, usuarioId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tarefa t = criarTarefaDoResultSet(rs);
                    tarefas.add(t);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tarefas com permissões", e);
        }
        return tarefas;
    }

    /**
     * Lista todas as tarefas do banco de dados.
     */
    public List<Tarefa> listarPorUsuario(int usuarioId) {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM tarefa WHERE responsavel_id = ? ORDER BY data_entrega ASC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tarefa t = criarTarefaDoResultSet(rs); // Extraí a lógica para não repetir código, veja abaixo
                    tarefas.add(t);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tarefas do usuário", e);
        }
        return tarefas;
    }
    
    /**
     * Lista todas as tarefas de um projeto (sem filtrar por responsável).
     */
    public List<Tarefa> listarTodasTarefasDoProjeto(int projetoId) {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql = "SELECT * FROM tarefa WHERE projeto_id = ? ORDER BY data_entrega ASC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projetoId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tarefa t = criarTarefaDoResultSet(rs);
                    tarefas.add(t);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar todas as tarefas do projeto", e);
        }
        return tarefas;
    }
    
    public void concluir(int id) {
        String sql = "UPDATE tarefa SET status_id = 3 WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao concluir tarefa", e);
        }
    }

    /**
     * Lista tarefas filtrando por um ID de Projeto específico com permissões.
     */
    public List<Tarefa> listarPorProjetoComPermissoes(int projetoId, int usuarioId, String funcaoUsuario) {
        List<Tarefa> tarefas = new ArrayList<>();
        String sql;
        
        // Se for Gerente, pode ver todas as tarefas do projeto
        if ("Gerente".equals(funcaoUsuario)) {
            sql = "SELECT * FROM tarefa WHERE projeto_id = ? ORDER BY data_entrega ASC";
        } else {
            // Usuário comum: só vê tarefas do projeto se tiver acesso ao projeto
            sql = "SELECT t.* FROM tarefa t " +
                  "INNER JOIN projeto p ON t.projeto_id = p.id " +
                  "LEFT JOIN projeto_usuario pu ON p.id = pu.projeto_id " +
                  "WHERE t.projeto_id = ? AND (p.lider_id = ? OR pu.usuario_id = ?) " +
                  "ORDER BY t.data_entrega ASC";
        }

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projetoId);
            if (!"Gerente".equals(funcaoUsuario)) {
                stmt.setInt(2, usuarioId);
                stmt.setInt(3, usuarioId);
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tarefa t = criarTarefaDoResultSet(rs);
                    tarefas.add(t);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tarefas por projeto com permissões", e);
        }
        return tarefas;
    }

    /**
     * Lista tarefas filtrando por um ID de Projeto específico.
     */
    public List<Tarefa> listarPorProjeto(int projetoId, int usuarioId) {
        List<Tarefa> tarefas = new ArrayList<>();
        // Note o AND responsavel_id = ?
        String sql = "SELECT * FROM tarefa WHERE projeto_id = ? AND responsavel_id = ? ORDER BY data_entrega ASC";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, projetoId);
            stmt.setInt(2, usuarioId); // Filtra pelo dono também
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Tarefa t = criarTarefaDoResultSet(rs);
                    tarefas.add(t);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tarefas por projeto", e);
        }
        return tarefas;
    }

    /**
     * Insere uma nova tarefa no banco.
     */
    public void inserir(Tarefa tarefa) {
        String sql = "INSERT INTO tarefa (nome, descricao, data_criacao, data_entrega, projeto_id, responsavel_id, status_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tarefa.getNome());
            stmt.setString(2, tarefa.getDescricao());

            // Data Criação
            if (tarefa.getDataCriacao() == null) {
                stmt.setDate(3, new java.sql.Date(System.currentTimeMillis()));
            } else {
                stmt.setDate(3, new java.sql.Date(tarefa.getDataCriacao().getTime()));
            }

            // Data Entrega
            if (tarefa.getDataEntrega() != null) {
                stmt.setDate(4, new java.sql.Date(tarefa.getDataEntrega().getTime()));
            } else {
                stmt.setNull(4, java.sql.Types.DATE);
            }

            // IDs (Projeto, Responsável, Status)
            if (tarefa.getProjetoId() != null) stmt.setInt(5, tarefa.getProjetoId());
            else stmt.setNull(5, java.sql.Types.INTEGER);

            if (tarefa.getResponsavelId() != null) stmt.setInt(6, tarefa.getResponsavelId());
            else stmt.setNull(6, java.sql.Types.INTEGER);

            if (tarefa.getStatusId() != null) stmt.setInt(7, tarefa.getStatusId());
            else stmt.setInt(7, 1); // Default Pendente

            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir tarefa", e);
        }
    }

    /**
     * Deleta uma tarefa pelo ID.
     */
    public void deletar(int id) {
        String sql = "DELETE FROM tarefa WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.execute();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar tarefa", e);
        }
    }
    
    private Tarefa criarTarefaDoResultSet(ResultSet rs) throws SQLException {
        Tarefa t = new Tarefa();
        t.setId(rs.getInt("id"));
        t.setNome(rs.getString("nome"));
        t.setDescricao(rs.getString("descricao"));
        t.setDataCriacao(rs.getDate("data_criacao"));
        t.setDataEntrega(rs.getDate("data_entrega"));
        t.setProjetoId(rs.getInt("projeto_id"));
        if (rs.wasNull()) t.setProjetoId(null); // Importante para o Integer
        t.setResponsavelId(rs.getInt("responsavel_id"));
        t.setStatusId(rs.getInt("status_id"));
        return t;
    }
    
}