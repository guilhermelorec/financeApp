package model;

/**
 * Representa um tipo de movimento (categoria) - ex: Energia Eletrica, Salario, Plano de Saude.
 * Os tipos sao mantidos em uma tabela propria, conforme requisito.
 */
public class TipoMovimento {

    private int id;
    private String descricao;
    private char natureza; // 'D' = Despesa, 'R' = Receita

    public TipoMovimento() {
    }

    public TipoMovimento(int id, String descricao, char natureza) {
        this.id = id;
        this.descricao = descricao;
        this.natureza = natureza;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public char getNatureza() {
        return natureza;
    }

    public void setNatureza(char natureza) {
        this.natureza = natureza;
    }

    public boolean isDespesa() {
        return natureza == 'D' || natureza == 'd';
    }

    @Override
    public String toString() {
        return id + " - " + descricao + " (" + (isDespesa() ? "Despesa" : "Receita") + ")";
    }
}
