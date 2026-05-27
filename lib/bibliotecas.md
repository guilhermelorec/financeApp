# `lib/` - Bibliotecas externas (jars)

Pasta para os jars usados em tempo de compilacao e execucao.

## Arquivos

| Arquivo | Para que serve |
|---------|-----------------|
| `postgresql-42.7.4.jar` | Driver JDBC para PostgreSQL |

## Por que estao versionados?

O projeto e didatico e pequeno. Para evitar a complexidade de Maven/Gradle
e garantir que qualquer pessoa rode com `javac` puro, optamos por
versionar o jar do driver no Git.

Em projetos reais, o ideal e usar um build tool com gerenciamento de
dependencias.

## Onde baixar

[https://jdbc.postgresql.org/download/](https://jdbc.postgresql.org/download/)

## Como o projeto referencia

- Compilacao:
  ```
  javac -d bin -cp "lib/*" ...
  ```
- Execucao:
  ```
  java -cp "bin;lib/*" app.Main
  ```

A factory de conexao [[../src/dao/acesso-banco|ConnectionFactory]] depende do
driver estar no classpath.

## Tags

#projeto/build #jdbc
