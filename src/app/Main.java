package app;

import view.MenuPrincipal;

/**
 * Classe principal - ponto de entrada da aplicacao FinanceApp.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== FinanceApp - Controle de Despesas e Receitas ===");
        new MenuPrincipal().exibir();
    }
}
