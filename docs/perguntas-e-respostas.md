# FinanceApp - Perguntas e respostas para a banca

Guia de estudo com perguntas que a banca pode fazer durante a apresentacao,
agrupadas por tema. Cada resposta e curta e ja referencia o arquivo do
projeto onde o assunto esta implementado.

---

## 1. Requisitos do trabalho

### 1.1 Onde no codigo voce usou expressao lambda?
Em `service/RelatorioService.java` (`totalDespesas`, `totalReceitas`,
`totalPendentes`) e em `view/CadastroMovimentoView.java`
(`escolherTipo` filtra por natureza).

Exemplo:
```java
movimentos.stream()
    .filter(m -> m.getTipoMovimento().isDespesa()) // <-- LAMBDA
    .map(Movimento::getValor)
    .reduce(BigDecimal.ZERO, BigDecimal::add);
```

### 1.2 Por que usar lambda em vez de um for tradicional?
- Mais declarativo: descreve **o que** se quer (filtrar/somar) e nao **como**.
- Menos codigo repetido (nao precisa de variavel acumuladora nem indice).
- Permite encadear `filter`, `map`, `reduce`, `count` via Stream API.

### 1.3 Onde voce usou Enum e por que?
- `model/enums/FormaPagamentoEnum.java`
- `model/enums/StatusDebito.java`

Motivos:
- Seguranca de tipo (impede strings "magicas" como `"cartao"` ou `"CARTAO "`).
- Comportamento agregado a cada constante (`exigeBanco`,
  `permiteParcelamento`).
- Facilita logica condicional (`if (forma.getTipo().isPermiteParcelamento())`).

### 1.4 Onde aparece ArrayList?
- `dao/MovimentoDAO.java` em `buscarPorPeriodo`.
- `dao/TipoMovimentoDAO.java` em `listarTodos`.

Sao instanciados como `new ArrayList<>()` para acumular o resultado do
`ResultSet`.

### 1.5 Por que ArrayList e nao LinkedList?
- O uso e "preencher uma vez no DAO, percorrer varias vezes depois".
- `ArrayList` tem acesso O(1) por indice e melhor localidade de memoria.
- `LinkedList` so seria vantajoso se houvesse muitas insercoes/remocoes
  no meio da lista, o que nao e o caso aqui.

### 1.6 O que e Value Object e onde voce usou?
Um objeto definido pelo valor dos atributos, sem identidade propria.

Em `dto/PeriodoRelatorioVO.java`:
- Classe `final` (nao pode ser estendida).
- Atributos `final` (imutaveis).
- Validacao no construtor (`dataInicial <= dataFinal`).
- `equals`/`hashCode` por valor.

Garante que dois periodos com as mesmas datas sejam tratados como iguais.

---

## 2. Arquitetura

### 2.1 Por que separar em view, service, dao?
- **view**: I/O com o usuario.
- **service**: regras e validacoes.
- **dao**: acesso a dados (SQL/JDBC).

Permite trocar a UI (console -> web) ou o banco sem reescrever as regras
de negocio.

### 2.2 Por que o DAO nao chama o Service?
Inverteria a dependencia. Fluxo padrao e "para baixo":
view -> service -> dao -> banco.

### 2.3 E se voce migrar de PostgreSQL para outro banco?
Apenas `MovimentoDAO`/`TipoMovimentoDAO`/`ConnectionFactory` mudariam.
`service`, `model`, `view` e `enums` nao dependem de SQL.

### 2.4 Por que usar try-with-resources?
Fecha automaticamente `Connection`, `PreparedStatement` e `ResultSet`
mesmo em caso de excecao. Crucial num pool como o do Supabase para evitar
vazamento de conexoes.

---

## 3. Banco de dados

### 3.1 Por que tipo_movimento em tabela separada e nao um enum?
- Foi requisito do trabalho.
- Permite cadastrar/editar tipos sem recompilar.
- `FormaPagamento` (Dinheiro/Cartao/Pix) e fechado no dominio, por isso
  virou enum.

### 3.2 Como voce garante a integridade entre movimento e tipo?
Foreign key `fk_movimento_tipo` em `movimento.tipo_movimento_id`
referenciando `tipo_movimento.id`.

### 3.3 Por que SERIAL e nao UUID?
- Suficiente para a escala do trabalho.
- Menor (4 bytes) e legivel para o usuario (o ID aparece no relatorio).
- UUID seria preferivel em sistemas distribuidos.

### 3.4 Por que NUMERIC(12,2) e nao FLOAT/DOUBLE?
Dinheiro **nunca** deve usar ponto flutuante. NUMERIC (Postgres) e
BigDecimal (Java) evitam erros de arredondamento binario, ex.
`0.1 + 0.2 != 0.3`.

### 3.5 Por que ha indice em data_movimento?
O relatorio faz `WHERE m.data_movimento BETWEEN ? AND ?`. O indice
acelera essa consulta.

### 3.6 Como voce protege contra SQL Injection?
Uso exclusivo de `PreparedStatement` com parametros (`?`). Nenhum valor
de usuario e concatenado na string SQL.

---

## 4. Conexao e seguranca

### 4.1 Onde estao as credenciais?
Hoje em `util/DatabaseConfig.java` (e `config.properties`). Ideal em
producao seria usar variaveis de ambiente. A classe util esta isolada
justamente para facilitar essa troca.

### 4.2 Por que sslmode=require?
O Supabase exige conexao criptografada. Sem isso o driver recusa.

### 4.3 Voce cria uma conexao por operacao. Nao seria melhor um pool?
- Para o escopo do trabalho, simplifica.
- Em producao usaria HikariCP ou o pooler do Supabase como fonte.
- Ja uso o **pooler do Supabase** no host (`pooler.supabase.com:5432`),
  o que minimiza o impacto.

---

## 5. Validacoes e UX

### 5.1 O que acontece se eu informar um valor negativo?
`MovimentoService.validar` lanca
`IllegalArgumentException("Valor deve ser maior que zero.")`.
A view captura e exibe `>> Erro de validacao: ...`.

### 5.2 E se a data for invalida?
`InputUtil.lerData` repete o prompt enquanto o formato `dd/MM/yyyy`
nao for valido.

### 5.3 Por que pedir natureza antes do tipo?
- Diminui a lista exibida.
- Evita erro do usuario escolher um tipo de receita pensando que e
  despesa. E filtragem progressiva.

### 5.4 Por que confirmar antes de excluir?
Operacao destrutiva e irreversivel. `EdicaoMovimentoView.excluir`
mostra resumo e pede S/N antes de chamar o DAO.

---

## 6. Funcionalidades especificas

### 6.1 Por que Pix e a vista mas Cartao permite parcelas?
Comportamento embutido em `FormaPagamentoEnum` (`permiteParcelamento`).
Para Pix/Dinheiro, o codigo fixa `parcelas=1`, `vencimento = data do
movimento` e `status = DEBITADO`.

### 6.2 Por que "Outros" virou "Outras Despesas" e "Outras Receitas"?
- A constraint `UNIQUE (descricao)` impede dois registros com nome
  "Outros".
- Para separar por natureza, criei contraparte para receita.
- Migracao em `sql/migrations/001_outros_split.sql` mantem dados
  existentes.

### 6.3 Como voce gera o PDF?
- Gero um HTML com `media="print"` em `util/PDFGenerator.java`.
- Oriento o usuario a usar "Imprimir -> Salvar como PDF" no navegador.
- Vantagem: zero dependencias externas (sem iText, PDFBox, etc.).

### 6.4 Por que BigDecimal em vez de double?
Precisao monetaria (mesma motivacao do NUMERIC).

---

## 7. Possiveis "pegadinhas"

### 7.1 O que aconteceria se duas pessoas usassem ao mesmo tempo?
- Nao ha controle de concorrencia no app.
- Postgres garante atomicidade de cada `INSERT/UPDATE/DELETE`.
- Para multiusuario seria necessario autenticacao e sessao.

### 7.2 Como evita um movimento de natureza divergente do tipo?
A natureza e atributo do **tipo**, nao do movimento. O movimento
referencia o tipo, entao nunca diverge.

### 7.3 Por que nao tem testes automatizados?
- Nao foi requisito do trabalho.
- Para evoluir: JUnit testando `RelatorioService` (lambdas com listas
  mockadas) e `MovimentoService.validar` (regras puras, sem banco).
- DAO precisaria de Testcontainers ou banco em memoria.

### 7.4 O que e "package-private" e por que voce usa?
Metodos sem modificador ficam visiveis apenas no mesmo pacote.
Em `CadastroMovimentoView`, isso permite que `EdicaoMovimentoView`
reaproveite `lerDadosMovimento` sem expor a logica para fora de `view`.

---

## 8. Cheat sheet de classes para abrir ao vivo

Se a banca pedir para mostrar codigo, esteja pronto para abrir:

| Conceito | Classe |
|----------|--------|
| Lambda + Stream | `service/RelatorioService.java` |
| Enum com comportamento | `model/enums/FormaPagamentoEnum.java` |
| Value Object completo | `dto/PeriodoRelatorioVO.java` |
| ArrayList em DAO | `dao/MovimentoDAO.java` |
| PreparedStatement (anti SQL Injection) | `dao/MovimentoDAO.java` (`SQL_UPDATE`) |
| Validacao de regras de negocio | `service/MovimentoService.java` (`validar`) |
| Confirmacao antes de excluir | `view/EdicaoMovimentoView.java` |

---

## 9. Demo de uma volta so (caso pratico)

Roteiro curto para mostrar tudo em uma execucao:

1. **Cadastrar** uma despesa de **Cartao** em **3 parcelas** (mostra enum,
   parcelamento, banco obrigatorio).
2. **Cadastrar** uma receita de **Salario** em dinheiro (mostra fluxo a
   vista).
3. **Editar** a despesa do passo 1 (mostra reaproveitamento da view de
   cadastro + validacoes).
4. **Relatorio** do mes atual: mostra tabela, totais por lambda e
   gera o HTML/PDF.
5. **Excluir** a receita do passo 2 (mostra confirmacao S/N).

Em ~3 minutos voce passa por todos os requisitos: lambda, enum,
ArrayList, VO, banco com FK, CRUD completo e PDF.
