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
            LocalDate dataMov = InputUtil.lerData("Data do movimento");

            TipoMovimento tipo = escolherTipo();
            if (tipo == null) {
                System.out.println(">> Cadastro cancelado: nenhum tipo selecionado.");
                return;
            }

            String descricao = InputUtil.lerTexto("Descricao: ");
            BigDecimal valor = InputUtil.lerDecimal("Valor (R$): ");

            FormaPagamento forma = escolherFormaPagamento();
            int parcelas = InputUtil.lerInteiro("Quantidade de parcelas: ");
            LocalDate dataVenc = InputUtil.lerData("Data de vencimento");
            boolean debitado = InputUtil.lerSimNao("Lancamento ja foi debitado?");

            Movimento m = new Movimento();
            m.setDataMovimento(dataMov);
            m.setTipoMovimento(tipo);
            m.setDescricao(descricao);
            m.setValor(valor);
            m.setFormaPagamento(forma);
            m.setQuantidadeParcelas(parcelas);
            m.setDataVencimento(dataVenc);
            m.setStatus(StatusDebito.fromBoolean(debitado));

            int id = service.cadastrar(m);
            System.out.println();
            System.out.println(">> Movimento cadastrado com sucesso! ID = " + id);

        } catch (IllegalArgumentException e) {
            System.out.println(">> Erro de validacao: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(">> Erro de banco de dados: " + e.getMessage());
        }
    }

    private TipoMovimento escolherTipo() throws SQLException {
        List<TipoMovimento> tipos = service.listarTipos();
        if (tipos.isEmpty()) {
            System.out.println(">> Nenhum tipo de movimento cadastrado.");
            return null;
        }

        System.out.println();
        System.out.println("Tipos disponiveis:");
        tipos.forEach(t -> System.out.println("  " + t)); // <-- LAMBDA tambem aqui

        int idEscolhido = InputUtil.lerInteiro("Informe o ID do tipo: ");
        return tipos.stream()
                .filter(t -> t.getId() == idEscolhido) // <-- LAMBDA
                .findFirst()
                .orElse(null);
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
