package br.com.taskmaster.dao;

import br.com.taskmaster.model.Projeto;
import br.com.taskmaster.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjetoDAO {

    public List<Projeto> listar() {
        List<Projeto> lista = new ArrayList<>();

        String sql = "SELECT id, nome, data_inicio, data_fim, lider_id FROM projeto ORDER BY nome";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Projeto p = new Projeto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDate("data_inicio") != null ? rs.getDate("data_inicio").toLocalDate() : null,
                    rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null,
                    rs.getInt("lider_id")
                );

                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    // Método para listar projetos com permissões baseadas no usuário
    public List<Projeto> listarPorUsuario(int usuarioId, String funcaoUsuario) {
        List<Projeto> lista = new ArrayList<>();
        String sql;
        
        // Se for Gerente, pode ver todos os projetos
        if ("Gerente".equals(funcaoUsuario)) {
            sql = "SELECT id, nome, data_inicio, data_fim, lider_id FROM projeto ORDER BY nome";
        } else {
            // Usuário comum: só vê projetos onde é líder ou membro
            sql = "SELECT DISTINCT p.id, p.nome, p.data_inicio, p.data_fim, p.lider_id " +
                  "FROM projeto p " +
                  "LEFT JOIN projeto_usuario pu ON p.id = pu.projeto_id " +
                  "WHERE p.lider_id = ? OR pu.usuario_id = ? " +
                  "ORDER BY p.nome";
        }

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (!"Gerente".equals(funcaoUsuario)) {
                stmt.setInt(1, usuarioId);
                stmt.setInt(2, usuarioId);
            }
            
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Projeto p = new Projeto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDate("data_inicio") != null ? rs.getDate("data_inicio").toLocalDate() : null,
                    rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null,
                    rs.getInt("lider_id")
                );
                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public Projeto buscarPorId(int id) {
        String sql = "SELECT id, nome, data_inicio, data_fim, lider_id FROM projeto WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Projeto p = new Projeto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDate("data_inicio") != null ? rs.getDate("data_inicio").toLocalDate() : null,
                    rs.getDate("data_fim") != null ? rs.getDate("data_fim").toLocalDate() : null,
                    rs.getInt("lider_id")
                );
                
                carregarMembros(p); 
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public void inserir(Projeto projeto) {
        String sql = "INSERT INTO projeto (nome, data_inicio, data_fim, lider_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, projeto.getNome());
            stmt.setDate(2, projeto.getDataInicio() != null ? java.sql.Date.valueOf(projeto.getDataInicio()) : null);
            stmt.setDate(3, projeto.getDataFim() != null ? java.sql.Date.valueOf(projeto.getDataFim()) : null);
            stmt.setInt(4, projeto.getLiderId());

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                salvarMembros(idGerado, projeto.getMembrosIds(), conn);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir: " + e.getMessage());
        }
    }
    
    public void deletar(int id) {
        String sqlVinculos = "DELETE FROM projeto_usuario WHERE projeto_id = ?";
        String sqlProjeto = "DELETE FROM projeto WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection()) {
            
            try (PreparedStatement stmt = conn.prepareStatement(sqlVinculos)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlProjeto)) {
                stmt.setInt(1, id);
                stmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar projeto: " + e.getMessage(), e);
        }
    }
    
    public void atualizar(Projeto projeto) {
        String sql = "UPDATE projeto SET nome = ?, data_inicio = ?, data_fim = ?, lider_id = ? WHERE id = ?";
        
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, projeto.getNome());
            stmt.setDate(2, projeto.getDataInicio() != null ? java.sql.Date.valueOf(projeto.getDataInicio()) : null);
            stmt.setDate(3, projeto.getDataFim() != null ? java.sql.Date.valueOf(projeto.getDataFim()) : null);
            stmt.setInt(4, projeto.getLiderId());
            stmt.setInt(5, projeto.getId());

            stmt.executeUpdate();

            salvarMembros(projeto.getId(), projeto.getMembrosIds(), conn);

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar: " + e.getMessage());
        }
    }
    
    private void salvarMembros(int projetoId, List<Integer> membrosIds, Connection conn) throws SQLException {
        String sqlDelete = "DELETE FROM projeto_usuario WHERE projeto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlDelete)) {
            stmt.setInt(1, projetoId);
            stmt.executeUpdate();
        }

        if (membrosIds != null && !membrosIds.isEmpty()) {
            String sqlInsert = "INSERT INTO projeto_usuario (projeto_id, usuario_id) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sqlInsert)) {
                for (Integer usuarioId : membrosIds) {
                    stmt.setInt(1, projetoId);
                    stmt.setInt(2, usuarioId);
                    stmt.executeUpdate();
                }
            }
        }
    }

    public void carregarMembros(Projeto projeto) {
        String sql = "SELECT usuario_id FROM projeto_usuario WHERE projeto_id = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, projeto.getId());
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
                projeto.adicionarMembro(rs.getInt("usuario_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
