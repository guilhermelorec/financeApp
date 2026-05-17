package model.enums;

/**
 * Indica se o lancamento ja foi efetivamente debitado/recebido (pago)
 * ou se esta pendente.
 */
public enum StatusDebito {

    DEBITADO("Debitado"),
    PENDENTE("Pendente");

    private final String descricao;

    StatusDebito(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static StatusDebito fromBoolean(boolean debitado) {
        return debitado ? DEBITADO : PENDENTE;
    }

    public boolean toBoolean() {
        return this == DEBITADO;
    }
}
