package model.enums;

/**
 * Formas de pagamento aceitas pelo sistema.
 * CARTAO e PIX exigem informacao complementar do banco.
 */
public enum FormaPagamentoEnum {

    DINHEIRO("Dinheiro", false),
    CARTAO  ("Cartao",   true),
    PIX     ("Pix",      true);

    private final String descricao;
    private final boolean exigeBanco;

    FormaPagamentoEnum(String descricao, boolean exigeBanco) {
        this.descricao = descricao;
        this.exigeBanco = exigeBanco;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isExigeBanco() {
        return exigeBanco;
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
