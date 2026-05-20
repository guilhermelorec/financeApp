package service;

import java.sql.SQLException;
import java.util.List;

import dao.MovimentoDAO;
import dao.TipoMovimentoDAO;
import model.Movimento;
import model.TipoMovimento;

/**
 * Camada de servico para os movimentos. Faz validacoes de negocio
 * e orquestra os DAOs.
 */
public class MovimentoService {

    private final MovimentoDAO movimentoDAO = new MovimentoDAO();
    private final TipoMovimentoDAO tipoMovimentoDAO = new TipoMovimentoDAO();

    public int cadastrar(Movimento m) throws SQLException {
        validar(m);
        return movimentoDAO.inserir(m);
    }

    public boolean atualizar(Movimento m) throws SQLException {
        if (m.getId() <= 0) {
            throw new IllegalArgumentException("ID do movimento e obrigatorio para atualizacao.");
        }
        validar(m);
        return movimentoDAO.atualizar(m);
    }

    public boolean excluir(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalido.");
        }
        return movimentoDAO.excluir(id);
    }

    public Movimento buscarPorId(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("ID invalido.");
        }
        return movimentoDAO.buscarPorId(id);
    }

    public List<Movimento> listarPorPeriodo(dto.PeriodoRelatorioVO periodo) throws SQLException {
        return movimentoDAO.buscarPorPeriodo(periodo);
    }

    public List<TipoMovimento> listarTipos() throws SQLException {
        return tipoMovimentoDAO.listarTodos();
    }

    public TipoMovimento buscarTipoPorId(int id) throws SQLException {
        return tipoMovimentoDAO.buscarPorId(id);
    }

    private void validar(Movimento m) {
        if (m == null) {
            throw new IllegalArgumentException("Movimento nao pode ser nulo.");
        }
        if (m.getDataMovimento() == null) {
            throw new IllegalArgumentException("Data do movimento e obrigatoria.");
        }
        if (m.getTipoMovimento() == null) {
            throw new IllegalArgumentException("Tipo do movimento e obrigatorio.");
        }
        if (m.getDescricao() == null || m.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("Descricao e obrigatoria.");
        }
        if (m.getValor() == null || m.getValor().signum() <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero.");
        }
        if (m.getFormaPagamento() == null || !m.getFormaPagamento().isValido()) {
            throw new IllegalArgumentException(
                    "Forma de pagamento invalida. Para CARTAO ou PIX informe o banco.");
        }
        if (m.getQuantidadeParcelas() < 1) {
            throw new IllegalArgumentException("Quantidade de parcelas deve ser >= 1.");
        }
        if (m.getDataVencimento() == null) {
            throw new IllegalArgumentException("Data de vencimento e obrigatoria.");
        }
        if (m.getStatus() == null) {
            throw new IllegalArgumentException("Status do debito e obrigatorio.");
        }
    }
}
