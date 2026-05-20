package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.PeriodoRelatorioVO;
import model.FormaPagamento;
import model.Movimento;
import model.TipoMovimento;
import model.enums.FormaPagamentoEnum;
import model.enums.StatusDebito;

/**
 * DAO responsavel pelo acesso a tabela movimento.
 */
public class MovimentoDAO {

    private static final String SQL_INSERT =
            "INSERT INTO movimento " +
            "(data_movimento, tipo_movimento_id, descricao, valor, forma_pagamento, " +
            " banco, quantidade_parcelas, data_vencimento, debitado) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_BUSCA_PERIODO =
            "SELECT m.id, m.data_movimento, m.descricao, m.valor, m.forma_pagamento, " +
            "       m.banco, m.quantidade_parcelas, m.data_vencimento, m.debitado, " +
            "       t.id AS tipo_id, t.descricao AS tipo_descricao, t.natureza AS tipo_natureza " +
            "  FROM movimento m " +
            "  JOIN tipo_movimento t ON t.id = m.tipo_movimento_id " +
            " WHERE m.data_movimento BETWEEN ? AND ? " +
            " ORDER BY m.data_movimento, m.id";

    private static final String SQL_BUSCA_POR_ID =
            "SELECT m.id, m.data_movimento, m.descricao, m.valor, m.forma_pagamento, " +
            "       m.banco, m.quantidade_parcelas, m.data_vencimento, m.debitado, " +
            "       t.id AS tipo_id, t.descricao AS tipo_descricao, t.natureza AS tipo_natureza " +
            "  FROM movimento m " +
            "  JOIN tipo_movimento t ON t.id = m.tipo_movimento_id " +
            " WHERE m.id = ?";

    private static final String SQL_UPDATE =
            "UPDATE movimento " +
            "   SET data_movimento = ?, tipo_movimento_id = ?, descricao = ?, valor = ?, " +
            "       forma_pagamento = ?, banco = ?, quantidade_parcelas = ?, " +
            "       data_vencimento = ?, debitado = ? " +
            " WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM movimento WHERE id = ?";

    public int inserir(Movimento m) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_INSERT, new String[] { "id" })) {

            ps.setDate  (1, Date.valueOf(m.getDataMovimento()));
            ps.setInt   (2, m.getTipoMovimento().getId());
            ps.setString(3, m.getDescricao());
            ps.setBigDecimal(4, m.getValor());
            ps.setString(5, m.getFormaPagamento().getTipo().name());
            ps.setString(6, m.getFormaPagamento().getBanco());
            ps.setInt   (7, m.getQuantidadeParcelas());
            ps.setDate  (8, Date.valueOf(m.getDataVencimento()));
            ps.setBoolean(9, m.isDebitado());

            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    m.setId(id);
                    return id;
                }
            }
        }
        return -1;
    }

    public List<Movimento> buscarPorPeriodo(PeriodoRelatorioVO periodo) throws SQLException {
        // ArrayList - requisito do trabalho
        List<Movimento> lista = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_BUSCA_PERIODO)) {

            ps.setDate(1, Date.valueOf(periodo.getDataInicial()));
            ps.setDate(2, Date.valueOf(periodo.getDataFinal()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(montar(rs));
                }
            }
        }
        return lista;
    }

    public Movimento buscarPorId(int id) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_BUSCA_POR_ID)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
            }
        }
        return null;
    }

    public boolean atualizar(Movimento m) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_UPDATE)) {

            ps.setDate  (1, Date.valueOf(m.getDataMovimento()));
            ps.setInt   (2, m.getTipoMovimento().getId());
            ps.setString(3, m.getDescricao());
            ps.setBigDecimal(4, m.getValor());
            ps.setString(5, m.getFormaPagamento().getTipo().name());
            ps.setString(6, m.getFormaPagamento().getBanco());
            ps.setInt   (7, m.getQuantidadeParcelas());
            ps.setDate  (8, Date.valueOf(m.getDataVencimento()));
            ps.setBoolean(9, m.isDebitado());
            ps.setInt   (10, m.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_DELETE)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private Movimento montar(ResultSet rs) throws SQLException {
        TipoMovimento tipo = new TipoMovimento();
        tipo.setId(rs.getInt("tipo_id"));
        tipo.setDescricao(rs.getString("tipo_descricao"));
        String natureza = rs.getString("tipo_natureza");
        tipo.setNatureza(natureza == null || natureza.isEmpty() ? 'D' : natureza.charAt(0));

        FormaPagamento forma = new FormaPagamento();
        forma.setTipo(FormaPagamentoEnum.fromString(rs.getString("forma_pagamento")));
        forma.setBanco(rs.getString("banco"));

        Movimento m = new Movimento();
        m.setId(rs.getInt("id"));
        m.setDataMovimento(rs.getDate("data_movimento").toLocalDate());
        m.setTipoMovimento(tipo);
        m.setDescricao(rs.getString("descricao"));
        m.setValor(rs.getBigDecimal("valor"));
        m.setFormaPagamento(forma);
        m.setQuantidadeParcelas(rs.getInt("quantidade_parcelas"));
        m.setDataVencimento(rs.getDate("data_vencimento").toLocalDate());
        m.setStatus(StatusDebito.fromBoolean(rs.getBoolean("debitado")));
        return m;
    }
}
