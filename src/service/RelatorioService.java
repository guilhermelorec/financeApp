package service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import dao.MovimentoDAO;
import dto.PeriodoRelatorioVO;
import model.Movimento;

/**
 * Servico responsavel pela geracao dos relatorios.
 *
 * IMPORTANTE: Este servico utiliza expressoes LAMBDA (requisito do trabalho)
 * para filtrar e somar valores via Stream API.
 */
public class RelatorioService {

    private final MovimentoDAO movimentoDAO = new MovimentoDAO();

    /**
     * Lista os movimentos de um periodo, ja vindos ordenados por data do banco.
     */
    public List<Movimento> listarPorPeriodo(PeriodoRelatorioVO periodo) throws SQLException {
        return movimentoDAO.buscarPorPeriodo(periodo);
    }

    /**
     * Calcula o total de despesas de uma lista de movimentos
     * usando expressao lambda + Stream API.
     */
    public BigDecimal totalDespesas(List<Movimento> movimentos) {
        return movimentos.stream()
                .filter(m -> m.getTipoMovimento().isDespesa()) // <-- LAMBDA
                .map(Movimento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calcula o total de receitas usando expressao lambda.
     */
    public BigDecimal totalReceitas(List<Movimento> movimentos) {
        return movimentos.stream()
                .filter(m -> !m.getTipoMovimento().isDespesa()) // <-- LAMBDA
                .map(Movimento::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Saldo do periodo (receitas - despesas).
     */
    public BigDecimal saldo(List<Movimento> movimentos) {
        return totalReceitas(movimentos).subtract(totalDespesas(movimentos));
    }

    /**
     * Total de itens ainda nao debitados.
     */
    public long totalPendentes(List<Movimento> movimentos) {
        return movimentos.stream()
                .filter(m -> !m.isDebitado()) // <-- LAMBDA
                .count();
    }
}
