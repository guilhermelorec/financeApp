package view;

import util.InputUtil;

/**
 * Menu principal da aplicacao - ponto de entrada da interface com o usuario.
 */
public class MenuPrincipal {

    private final CadastroMovimentoView cadastroView = new CadastroMovimentoView();
    private final RelatorioView         relatorioView = new RelatorioView();
    private final EdicaoMovimentoView   edicaoView    = new EdicaoMovimentoView();

    public void exibir() {
        boolean continuar = true;

        while (continuar) {
            imprimirMenu();
            int opcao = InputUtil.lerInteiro("Escolha uma opcao: ");

            switch (opcao) {
                case 1:
                    cadastroView.executar();
                    break;
                case 2:
                    relatorioView.executar();
                    break;
                case 3:
                    edicaoView.editar();
                    break;
                case 4:
                    edicaoView.excluir();
                    break;
                case 0:
                    continuar = false;
                    break;
                default:
                    System.out.println(">> Opcao invalida.");
            }
        }

        System.out.println();
        System.out.println("Encerrando o FinanceApp. Ate logo!");
    }

    private void imprimirMenu() {
        System.out.println();
        System.out.println("===========================================");
        System.out.println("           FinanceApp - Menu");
        System.out.println("===========================================");
        System.out.println(" 1 - Cadastrar movimento");
        System.out.println(" 2 - Relatorio por periodo");
        System.out.println(" 3 - Editar movimento");
        System.out.println(" 4 - Excluir movimento");
        System.out.println(" 0 - Sair");
        System.out.println("===========================================");
    }
}
