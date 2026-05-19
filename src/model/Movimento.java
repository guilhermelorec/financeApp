package model;

import java.math.BigDecimal;
import java.time.LocalDate;

import model.enums.StatusDebito;

/**
 * Lancamento financeiro (despesa ou receita).
 *
 * Atende aos requisitos minimos do enunciado:
 * - Data do movimento, Tipo, Descricao, Valor, Forma de pagamento,
 *   Quantidade de parcelas, Data de vencimento e Status (debitado?).
 */
public class Movimento {

    private int id;
    private LocalDate dataMovimento;
    private TipoMovimento tipoMovimento;
    private String descricao;
    private BigDecimal valor;
    private FormaPagamento formaPagamento;
    private int quantidadeParcelas;
    private LocalDate dataVencimento;
    private StatusDebito status;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDataMovimento() { return dataMovimento; }
    public void setDataMovimento(LocalDate dataMovimento) { this.dataMovimento = dataMovimento; }

    public TipoMovimento getTipoMovimento() { return tipoMovimento; }
    public void setTipoMovimento(TipoMovimento tipoMovimento) { this.tipoMovimento = tipoMovimento; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public int getQuantidadeParcelas() { return quantidadeParcelas; }
    public void setQuantidadeParcelas(int quantidadeParcelas) { this.quantidadeParcelas = quantidadeParcelas; }

    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }

    public StatusDebito getStatus() { return status; }
    public void setStatus(StatusDebito status) { this.status = status; }

    public boolean isDebitado() {
        return status != null && status.toBoolean();
    }
}
