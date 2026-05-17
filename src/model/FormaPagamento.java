package model;

import model.enums.FormaPagamentoEnum;

/**
 * Forma de pagamento utilizada no movimento.
 * Quando o tipo for CARTAO ou PIX, o atributo "banco" e obrigatorio.
 */
public class FormaPagamento {

    private FormaPagamentoEnum tipo;
    private String banco;

    public FormaPagamento() {
    }

    public FormaPagamento(FormaPagamentoEnum tipo, String banco) {
        this.tipo = tipo;
        this.banco = banco;
    }

    public FormaPagamentoEnum getTipo() {
        return tipo;
    }

    public void setTipo(FormaPagamentoEnum tipo) {
        this.tipo = tipo;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    /**
     * Valida se o banco esta preenchido quando exigido pelo tipo.
     */
    public boolean isValido() {
        if (tipo == null) {
            return false;
        }
        if (tipo.isExigeBanco()) {
            return banco != null && !banco.trim().isEmpty();
        }
        return true;
    }

    @Override
    public String toString() {
        if (tipo == null) {
            return "";
        }
        if (tipo.isExigeBanco() && banco != null) {
            return tipo.getDescricao() + " (" + banco + ")";
        }
        return tipo.getDescricao();
    }
}
