package model;

import model.enums.FormaPagamentoEnum;

/**
 * Forma de pagamento utilizada no movimento.
 * Quando o tipo for CARTAO ou PIX, o atributo "banco" e obrigatorio.
 *
 * Classe imutavel: o tipo e o banco sao definidos no construtor e nao mudam.
 */
public class FormaPagamento {

    private final FormaPagamentoEnum tipo;
    private final String banco;

    public FormaPagamento(FormaPagamentoEnum tipo, String banco) {
        this.tipo = tipo;
        this.banco = banco;
    }

    public FormaPagamentoEnum getTipo() {
        return tipo;
    }

    public String getBanco() {
        return banco;
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
