# `service/` - Regras de negocio

Camada que orquestra DAOs e aplica validacoes/regras antes de gravar
ou consultar dados.

## Arquivos

| Arquivo | Responsabilidade |
|---------|-------------------|
| `MovimentoService.java` | CRUD de movimento + validacoes |
| `RelatorioService.java` | Calculos do relatorio (totais, saldo) com **Stream/Lambda** |

## `MovimentoService`

Operacoes:
- `cadastrar(Movimento m)` - valida e insere.
- `atualizar(Movimento m)` - valida e atualiza por ID.
- `excluir(int id)` - exclui por ID.
- `buscarPorId(int id)` - busca um registro completo.
- `listarTipos()` - usado pela view para escolha do tipo.

Validacoes em `validar(Movimento m)`:
- Data, descricao, valor &gt; 0, tipo, status, vencimento, parcelas &gt;= 1.
- Forma de pagamento valida (banco obrigatorio para Cartao/Pix).

Lanca `IllegalArgumentException` em caso de violacao.

## `RelatorioService` - destaque para Lambdas

Todas as agregacoes usam Stream API + lambda:

```java
public BigDecimal totalDespesas(List<Movimento> movimentos) {
    return movimentos.stream()
            .filter(m -> m.getTipoMovimento().isDespesa()) // <- LAMBDA
            .map(Movimento::getValor)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}

public long totalPendentes(List<Movimento> movimentos) {
    return movimentos.stream()
            .filter(m -> !m.isDebitado()) // <- LAMBDA
            .count();
}
```

Operacoes disponiveis:
- `listarPorPeriodo(periodo)`
- `totalReceitas(lista)`
- `totalDespesas(lista)`
- `saldo(lista)`
- `totalPendentes(lista)`

## Por que esta camada existe?

- Centraliza regras de negocio (uma so validacao para cadastro e edicao).
- Permite reuso pela view (cadastro chama o mesmo `validar` que a edicao).
- Isola a [[../view/telas-console|view]] do [[../dao/acesso-banco|DAO]].
- Facilita testar regras sem subir banco.

## Tags

#projeto/codigo #java/service #lambda #stream
