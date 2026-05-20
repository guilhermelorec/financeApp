package view;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import model.Movimento;
import service.MovimentoService;
import util.InputUtil;

/**
 * Tela (console) responsavel pela edicao e exclusao de um movimento existente.
 */
public class EdicaoMovimentoView {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final MovimentoService service = new MovimentoService();
    private final CadastroMovimentoView cadastroView = new CadastroMovimentoView();

    public void editar() {
        System.out.println();
        System.out.println("====== Edicao de Movimento ======");

        try {
            Movimento atual = buscar();
            if (atual == null) {
                return;
            }

            exibirResumo(atual);
            if (!InputUtil.lerSimNao("Deseja editar este movimento?")) {
                System.out.println(">> Edicao cancelada.");
                return;
            }

            System.out.println();
            System.out.println("Informe os novos dados:");
            Movimento atualizado = cadastroView.lerDadosMovimento(atual);
            if (atualizado == null) {
                return;
            }

            if (service.atualizar(atualizado)) {
                System.out.println();
                System.out.println(">> Movimento atualizado com sucesso! ID = " + atualizado.getId());
            } else {
                System.out.println(">> Nenhum registro foi atualizado.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(">> Erro de validacao: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(">> Erro de banco de dados: " + e.getMessage());
        }
    }

    public void excluir() {
        System.out.println();
        System.out.println("====== Exclusao de Movimento ======");

        try {
            Movimento atual = buscar();
            if (atual == null) {
                return;
            }

            exibirResumo(atual);
            if (!InputUtil.lerSimNao("Confirma a exclusao deste movimento?")) {
                System.out.println(">> Exclusao cancelada.");
                return;
            }

            if (service.excluir(atual.getId())) {
                System.out.println(">> Movimento excluido com sucesso! ID = " + atual.getId());
            } else {
                System.out.println(">> Nenhum registro foi excluido.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println(">> Erro de validacao: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println(">> Erro de banco de dados: " + e.getMessage());
        }
    }

    private Movimento buscar() throws SQLException {
        int id = InputUtil.lerInteiro("Informe o ID do movimento: ");
        Movimento m = service.buscarPorId(id);
        if (m == null) {
            System.out.println(">> Movimento nao encontrado para o ID " + id + ".");
            return null;
        }
        return m;
    }

    private void exibirResumo(Movimento m) {
        System.out.println();
        System.out.println("--- Movimento atual ---");
        System.out.println("ID:         " + m.getId());
        System.out.println("Data:       " + m.getDataMovimento().format(FMT));
        System.out.println("Tipo:       " + m.getTipoMovimento().getDescricao());
        System.out.println("Descricao:  " + m.getDescricao());
        System.out.println("Valor:      R$ " + m.getValor().toPlainString());
        System.out.println("Forma:      " + m.getFormaPagamento());
        System.out.println("Parcelas:   " + m.getQuantidadeParcelas());
        System.out.println("Vencimento: " + m.getDataVencimento().format(FMT));
        System.out.println("Status:     " + m.getStatus().getDescricao());
        System.out.println();
    }
}
