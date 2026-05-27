# `app/` - Ponto de entrada

Pasta com a classe que inicia a aplicacao.

## Arquivos

### `Main.java`

Classe `app.Main`. Metodo `main` simples:

```java
public static void main(String[] args) {
    System.out.println("=== FinanceApp - Controle de Despesas e Receitas ===");
    new MenuPrincipal().exibir();
}
```

Apenas instancia [[../view/telas-console|MenuPrincipal]] e delega o controle.

## Por que separar `app` das demais?

- Convencao: pacote `app` so para o "main", evitando misturar com regras
  ou apresentacao.
- Facilita identificar onde a aplicacao comeca.

## Tags

#projeto/codigo #java/main
