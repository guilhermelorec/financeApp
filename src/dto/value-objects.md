# `dto/` - Data Transfer Objects e Value Objects

Pasta para objetos auxiliares que transitam entre camadas.

## Arquivos

| Arquivo | Tipo |
|---------|------|
| `PeriodoRelatorioVO.java` | **Value Object** - encapsula data inicial/final |

## `PeriodoRelatorioVO`

Caracteristicas de Value Object aplicadas:

- Classe `final` (nao pode ser estendida).
- Atributos `final` (imutavel apos construcao).
- Sem setters - nao da para alterar depois de criado.
- Validacao no construtor:
  - Datas obrigatorias.
  - Data final &gt;= data inicial.
- `equals()` e `hashCode()` baseados nos valores.
- `toString()` formatado: `"Periodo dd/MM/yyyy ate dd/MM/yyyy"`.

Metodo extra: `contem(LocalDate data)` - verifica se uma data esta no periodo.

## Por que Value Object?

- **Imutavel** -&gt; thread-safe e seguro para usar como chave de cache/Map.
- **Identidade por valor** -&gt; dois periodos com as mesmas datas sao iguais.
- **Auto-validavel** -&gt; impossivel ter um VO em estado invalido.

Padrao classico do livro "Domain-Driven Design" (Eric Evans).

## Diferenca DTO vs VO

| | DTO | VO |
|---|-----|-----|
| Mutabilidade | Pode ser mutavel | Sempre imutavel |
| Identidade | Sem identidade | Por valor |
| Comportamento | Apenas dados | Pode ter metodos auxiliares |
| Validacao | Rara | No construtor |

`PeriodoRelatorioVO` e VO. Outros DTOs poderiam ser adicionados aqui no futuro.

## Tags

#projeto/codigo #java/vo #ddd
