package model.enums;

/**
 * Formas de pagamento aceitas pelo sistema.
 *
 * - CARTAO e PIX exigem informacao complementar do banco.
 * - Apenas CARTAO permite parcelamento e tem data de vencimento futura
 *   (Pix e Dinheiro sao a vista, parcela = 1 e vencimento = data do movimento).
 */
public enum FormaPagamentoEnum {

    DINHEIRO("Dinheiro", false, false),
    CARTAO  ("Cartao",   true,  true),
    PIX     ("Pix",      true,  false);

    private final String descricao;
    private final boolean exigeBanco;
    private final boolean permiteParcelamento;

    FormaPagamentoEnum(String descricao, boolean exigeBanco, boolean permiteParcelamento) {
        this.descricao = descricao;
        this.exigeBanco = exigeBanco;
        this.permiteParcelamento = permiteParcelamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isExigeBanco() {
        return exigeBanco;
    }

    public boolean isPermiteParcelamento() {
        return permiteParcelamento;
    }

    /**
     * Converte uma string vinda do banco/usuario para o enum correspondente.
     */
    public static FormaPagamentoEnum fromString(String valor) {
        if (valor == null) {
            return null;
        }
        return FormaPagamentoEnum.valueOf(valor.trim().toUpperCase());
    }
}
