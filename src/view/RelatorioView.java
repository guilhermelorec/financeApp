package view;

import java.io.File;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dto.PeriodoRelatorioVO;
import model.Movimento;
import service.RelatorioService;
import util.InputUtil;
import util.PDFGenerator;

/**
 * Tela (console) responsavel por exibir o relatorio de movimentos
 * dentro de um periodo e oferecer geracao de PDF.
 */
public class RelatorioView {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final RelatorioService service = new RelatorioService();

    public void executar() {
        System.out.println();
        System.out.println("====== Relatorio de Movimentos ======");

        try {
            LocalDate inicio = InputUtil.lerData("Data inicial");
            LocalDate fim    = InputUtil.lerData("Data final");

            PeriodoRelatorioVO periodo = new PeriodoRelatorioVO(inicio, fim);
            List<Movimento> movimentos = service.listarPorPeriodo(periodo);

            imprimirNoConsole(periodo, movimentos);

            BigDecimal totalReceitas = service.totalReceitas(movimentos);
            BigDecimal totalDespesas = service.totalDespesas(movimentos);
            BigDecimal saldo         = service.saldo(movimentos);
            long pendentes           = service.totalPendentes(movimentos);

            System.out.println();
            System.out.println("Total de Receitas: R$ " + totalReceitas);
            System.out.println("Total de Despesas: R$ " + totalDespesas);
            System.out.println("Saldo do periodo:  R$ " + saldo);
            System.out.println("Lancamentos pendentes (nao debitados): " + pendentes);

            if (!movimentos.isEmpty()
                    && InputUtil.lerSimNao("Deseja gerar o relatorio em PDF (HTML imprimivel)?")) {
                File arquivo = PDFGenerator.gerar(periodo, movimentos,
                        totalReceitas, totalDespesas, saldo);
                System.out.println(">> Relatorio gerado em: " + arquivo.getAbsolutePath());
                System.out.println(">> Abra no navegador e use 'Imprimir > Salvar como PDF'.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(">> Erro: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(">> Erro de banco de dados: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(">> Erro inesperado: " + e.getMessage());
        }
    }

    private void imprimirNoConsole(PeriodoRelatorioVO periodo, List<Movimento> movimentos) {
        System.out.println();
        System.out.println(periodo);
        System.out.println();

        if (movimentos.isEmpty()) {
            System.out.println("Nenhum movimento encontrado no periodo.");
            return;
        }

        String header = String.format("%-4s %-10s %-20s %-25s %-12s %-15s %-5s %-10s %-10s",
                "ID", "Data", "Tipo", "Descricao", "Valor", "Forma", "Parc.", "Vencto", "Status");
        System.out.println(header);
        System.out.println("-".repeat(header.length()));

        for (Movimento m : movimentos) {
            System.out.println(String.format("%-4d %-10s %-20s %-25s %-12s %-15s %-5d %-10s %-10s",
                    m.getId(),
                    m.getDataMovimento().format(FMT),
                    truncar(m.getTipoMovimento().getDescricao(), 20),
                    truncar(m.getDescricao(), 25),
                    m.getValor().toPlainString(),
                    truncar(m.getFormaPagamento().toString(), 15),
                    m.getQuantidadeParcelas(),
                    m.getDataVencimento().format(FMT),
                    m.getStatus().getDescricao()));
        }
    }

    private String truncar(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 1) + ".";
    }
}
