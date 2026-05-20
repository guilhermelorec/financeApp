package view;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import model.FormaPagamento;
import model.Movimento;
import model.TipoMovimento;
import model.enums.FormaPagamentoEnum;
import model.enums.StatusDebito;
import service.MovimentoService;
import util.InputUtil;

/**
 * Tela (console) responsavel pelo cadastro de um novo movimento.
 */
public class CadastroMovimentoView {

    private final MovimentoService service = new MovimentoService();

    public void executar() {
        System.out.println();
        System.out.println("====== Cadastro de Movimento ======");

        try {
            Movimento m = lerDadosMovimento(null);
            if (m == null) {
                return;
            }
            int id = service.cadastrar(m);
            System.out.println();
            System.out.println(">> Movimento cadastrado com sucesso! ID = " + id);

        } catch (IllegalArgumentException e) {
            System.out.println(">> Erro de validacao: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(">> Erro de banco de dados: " + e.getMessage());
        }
    }

    /**
     * Le os dados do movimento a partir do console. Quando "atual" e fornecido,
     * os valores existentes sao usados como sugestao (modo edicao).
     *
     * @param atual movimento atual (modo edicao) ou null para novo cadastro
     * @return o movimento preenchido, ou null caso o usuario cancele
     */
    public Movimento lerDadosMovimento(Movimento atual) throws SQLException {
        LocalDate dataMov = InputUtil.lerData("Data do movimento");

        TipoMovimento tipo = escolherTipo();
        if (tipo == null) {
            System.out.println(">> Operacao cancelada: nenhum tipo selecionado.");
            return null;
        }

        String descricao = InputUtil.lerTexto("Descricao: ");
        BigDecimal valor = InputUtil.lerDecimal("Valor (R$): ");

        FormaPagamento forma = escolherFormaPagamento();

        int parcelas;
        LocalDate dataVenc;
        boolean debitado;

        if (forma.getTipo().isPermiteParcelamento()) {
            parcelas = InputUtil.lerInteiro("Quantidade de parcelas: ");
            dataVenc = InputUtil.lerData("Data de vencimento");
            debitado = InputUtil.lerSimNao("Lancamento ja foi debitado?");
        } else {
            parcelas = 1;
            dataVenc = dataMov;
            debitado = true;
        }

        Movimento m = (atual != null) ? atual : new Movimento();
        m.setDataMovimento(dataMov);
        m.setTipoMovimento(tipo);
        m.setDescricao(descricao);
        m.setValor(valor);
        m.setFormaPagamento(forma);
        m.setQuantidadeParcelas(parcelas);
        m.setDataVencimento(dataVenc);
        m.setStatus(StatusDebito.fromBoolean(debitado));
        return m;
    }

    private TipoMovimento escolherTipo() throws SQLException {
        List<TipoMovimento> tipos = service.listarTipos();
        if (tipos.isEmpty()) {
            System.out.println(">> Nenhum tipo de movimento cadastrado.");
            return null;
        }

        boolean despesa = escolherNatureza();

        List<TipoMovimento> filtrados = tipos.stream()
                .filter(t -> t.isDespesa() == despesa) // <-- LAMBDA
                .toList();

        if (filtrados.isEmpty()) {
            System.out.println(">> Nenhum tipo de movimento cadastrado para "
                    + (despesa ? "Despesa" : "Receita") + ".");
            return null;
        }

        System.out.println();
        System.out.println("Tipos de " + (despesa ? "Despesa" : "Receita") + ":");
        filtrados.forEach(t -> System.out.println("  " + t)); // <-- LAMBDA

        int idEscolhido = InputUtil.lerInteiro("Informe o ID do tipo: ");
        return filtrados.stream()
                .filter(t -> t.getId() == idEscolhido) // <-- LAMBDA
                .findFirst()
                .orElse(null);
    }

    /**
     * Pergunta ao usuario se o movimento e Despesa ou Receita.
     * @return true para Despesa, false para Receita
     */
    private boolean escolherNatureza() {
        System.out.println();
        System.out.println("Natureza do movimento:");
        System.out.println("  1 - Despesa");
        System.out.println("  2 - Receita");
        while (true) {
            int op = InputUtil.lerInteiro("Escolha: ");
            if (op == 1) return true;
            if (op == 2) return false;
            System.out.println(">> Opcao invalida.");
        }
    }

    private FormaPagamento escolherFormaPagamento() {
        System.out.println();
        System.out.println("Formas de pagamento:");
        for (FormaPagamentoEnum fp : FormaPagamentoEnum.values()) {
            System.out.println("  " + (fp.ordinal() + 1) + " - " + fp.getDescricao());
        }

        int opcao;
        FormaPagamentoEnum tipo;
        while (true) {
            opcao = InputUtil.lerInteiro("Escolha: ");
            if (opcao >= 1 && opcao <= FormaPagamentoEnum.values().length) {
                tipo = FormaPagamentoEnum.values()[opcao - 1];
                break;
            }
            System.out.println(">> Opcao invalida.");
        }

        String banco = null;
        if (tipo.isExigeBanco()) {
            banco = InputUtil.lerTexto("Banco: ");
        }
        return new FormaPagamento(tipo, banco);
    }
}
