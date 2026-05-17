package util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import dto.PeriodoRelatorioVO;
import model.Movimento;

/**
 * Gera o relatorio em formato HTML (com layout de impressao - imprimivel como PDF
 * pelo navegador via "Salvar como PDF"). Optamos por nao usar bibliotecas externas
 * para manter o projeto simples e sem dependencias adicionais.
 */
public final class PDFGenerator {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private PDFGenerator() {
        // utilitario
    }

    public static File gerar(PeriodoRelatorioVO periodo,
                             List<Movimento> movimentos,
                             BigDecimal totalReceitas,
                             BigDecimal totalDespesas,
                             BigDecimal saldo) throws IOException {

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html><html lang='pt-BR'><head><meta charset='UTF-8'>");
        html.append("<title>Relatorio FinanceApp</title>");
        html.append("<style>")
            .append("body{font-family:Arial,sans-serif;margin:30px;color:#222}")
            .append("h1{text-align:center;color:#2c3e50}")
            .append(".info{text-align:center;margin-bottom:20px}")
            .append("table{width:100%;border-collapse:collapse;margin-top:10px}")
            .append("th,td{border:1px solid #888;padding:6px 8px;font-size:12px}")
            .append("th{background:#2c3e50;color:#fff;text-align:left}")
            .append("tr:nth-child(even){background:#f3f3f3}")
            .append(".totais{margin-top:20px;font-size:14px}")
            .append(".totais div{margin:4px 0}")
            .append(".positivo{color:#1b7e1b}")
            .append(".negativo{color:#b71c1c}")
            .append("@media print { body{margin:10px} }")
            .append("</style></head><body>");

        html.append("<h1>Relatorio de Movimentacoes</h1>");
        html.append("<div class='info'>").append(periodo.toString()).append("</div>");

        html.append("<table><thead><tr>")
            .append("<th>ID</th><th>Data Mov.</th><th>Tipo</th><th>Descricao</th>")
            .append("<th>Valor (R$)</th><th>Forma</th><th>Parc.</th>")
            .append("<th>Vencimento</th><th>Status</th>")
            .append("</tr></thead><tbody>");

        for (Movimento m : movimentos) {
            html.append("<tr>")
                .append("<td>").append(m.getId()).append("</td>")
                .append("<td>").append(m.getDataMovimento().format(FMT)).append("</td>")
                .append("<td>").append(m.getTipoMovimento().getDescricao()).append("</td>")
                .append("<td>").append(escape(m.getDescricao())).append("</td>")
                .append("<td>").append(m.getValor()).append("</td>")
                .append("<td>").append(m.getFormaPagamento().toString()).append("</td>")
                .append("<td>").append(m.getQuantidadeParcelas()).append("</td>")
                .append("<td>").append(m.getDataVencimento().format(FMT)).append("</td>")
                .append("<td>").append(m.getStatus().getDescricao()).append("</td>")
                .append("</tr>");
        }
        html.append("</tbody></table>");

        html.append("<div class='totais'>")
            .append("<div><strong>Total de Receitas:</strong> R$ ").append(totalReceitas).append("</div>")
            .append("<div><strong>Total de Despesas:</strong> R$ ").append(totalDespesas).append("</div>")
            .append("<div class='").append(saldo.signum() >= 0 ? "positivo" : "negativo").append("'>")
            .append("<strong>Saldo do Periodo:</strong> R$ ").append(saldo).append("</div>")
            .append("</div>");

        html.append("<script>window.onload=function(){window.print();}</script>");
        html.append("</body></html>");

        File arquivo = new File("relatorio_financeapp.html");
        try (FileWriter fw = new FileWriter(arquivo)) {
            fw.write(html.toString());
        }

        // Tenta abrir automaticamente no navegador padrao - imprimir como PDF.
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(arquivo.toURI());
            }
        } catch (Exception ignored) {
            // Falha ao abrir nao e fatal - o arquivo ja foi salvo.
        }

        return arquivo;
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}
