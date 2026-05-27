# `sql/migrations/` - Migracoes incrementais

Scripts para atualizar bancos **ja existentes** sem precisar recriar tudo.

## Convencao de nomes

```
NNN_descricao.sql
```

Onde `NNN` e um numero sequencial com 3 digitos (`001`, `002`, ...).

## Arquivos

| Arquivo | Descricao |
|---------|-----------|
| `001_outros_split.sql` | Separa `"Outros"` em `"Outras Despesas"` e `"Outras Receitas"` |

## Boas praticas adotadas

- **Idempotente**: usa `WHERE NOT EXISTS`/`UPDATE WHERE` para poder
  rodar mais de uma vez sem erro.
- **Somente DML/ajustes de seed** quando possivel (ja que o
  [[../scripts-sql|script principal]] cuida do schema).
- Numeracao crescente para deixar a ordem de execucao explicita.

## Como aplicar

1. Verifique qual a ultima migracao ja aplicada no seu banco.
2. Rode no SQL Editor do Supabase, em ordem, as migracoes posteriores.

## Tags

#projeto/banco #sql/migration
