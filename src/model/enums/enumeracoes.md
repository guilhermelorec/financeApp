# `model/enums/` - Enumeracoes do dominio

Conjuntos fechados de valores com comportamento embutido.

## Arquivos

| Arquivo | Constantes |
|---------|------------|
| `FormaPagamentoEnum.java` | `DINHEIRO`, `CARTAO`, `PIX` |
| `StatusDebito.java` | `DEBITADO`, `PENDENTE` |

## `FormaPagamentoEnum`

Cada constante carrega tres atributos:

| Constante | descricao | exigeBanco | permiteParcelamento |
|-----------|-----------|------------|---------------------|
| `DINHEIRO` | "Dinheiro" | false | false |
| `CARTAO`   | "Cartao"   | true  | true  |
| `PIX`      | "Pix"      | true  | false |

Metodo `fromString(valor)` converte a string vinda do banco para a constante.

Vantagem: a logica "Cartao permite parcelamento" fica embutida no enum,
nao espalhada em `if`/`switch` pelo codigo.

## `StatusDebito`

Constantes:
- `DEBITADO` ("Debitado")
- `PENDENTE` ("Pendente")

Metodos:
- `fromBoolean(boolean)` - conversao a partir de `boolean` do banco.
- `toBoolean()` - conversao reversa.

Util para mapear `BOOLEAN` do PostgreSQL e exibir descricao ao usuario.

## Por que enums e nao constantes?

- Seguranca de tipo: nao da para passar uma string invalida.
- Comportamento agregado: cada constante "sabe" se exige banco etc.
- IDE-friendly: autocomplete lista todas as opcoes.

## Tags

#projeto/codigo #java/enum #dominio
