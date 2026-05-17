package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.TipoMovimento;

/**
 * DAO responsavel pelo acesso a tabela tipo_movimento.
 */
public class TipoMovimentoDAO {

    public List<TipoMovimento> listarTodos() throws SQLException {
        // ArrayList - requisito do trabalho
        List<TipoMovimento> tipos = new ArrayList<>();

        String sql = "SELECT id, descricao, natureza FROM tipo_movimento ORDER BY descricao";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoMovimento tipo = new TipoMovimento();
                tipo.setId(rs.getInt("id"));
                tipo.setDescricao(rs.getString("descricao"));
                String natureza = rs.getString("natureza");
                tipo.setNatureza(natureza == null || natureza.isEmpty() ? 'D' : natureza.charAt(0));
                tipos.add(tipo);
            }
        }
        return tipos;
    }

    public TipoMovimento buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, descricao, natureza FROM tipo_movimento WHERE id = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    TipoMovimento tipo = new TipoMovimento();
                    tipo.setId(rs.getInt("id"));
                    tipo.setDescricao(rs.getString("descricao"));
                    String natureza = rs.getString("natureza");
                    tipo.setNatureza(natureza == null || natureza.isEmpty() ? 'D' : natureza.charAt(0));
                    return tipo;
                }
            }
        }
        return null;
    }

    public int inserir(TipoMovimento tipo) throws SQLException {
        String sql = "INSERT INTO tipo_movimento (descricao, natureza) VALUES (?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[] { "id" })) {

            ps.setString(1, tipo.getDescricao());
            ps.setString(2, String.valueOf(tipo.getNatureza()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    tipo.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }
}
