package util;

/**
 * Centraliza as configuracoes de acesso ao banco de dados PostgreSQL (Supabase).
 *
 * Como obter os dados de conexao no Supabase:
 *  1. Painel do projeto > Project Settings > Database
 *  2. Em "Connection string" selecione "JDBC".
 *  3. Substitua o host, usuario e senha abaixo pelos valores exibidos.
 *
 * Recomendacoes:
 *  - Use o "Session pooler" (porta 5432) ou o "Transaction pooler" (porta 6543)
 *    do Supabase para evitar problemas com IPv6.
 *  - O parametro sslmode=require e obrigatorio no Supabase.
 */
public final class DatabaseConfig {

    // Exemplo (Session pooler): aws-0-sa-east-1.pooler.supabase.com
    public static final String HOST     = "SEU_HOST.pooler.supabase.com";
    public static final int    PORTA    = 5432;
    public static final String DATABASE = "postgres";
    public static final String USUARIO  = "postgres.SEU_PROJECT_REF";
    public static final String SENHA    = "SUA_SENHA";

    public static final String URL =
            "jdbc:postgresql://" + HOST + ":" + PORTA + "/" + DATABASE
          + "?sslmode=require";

    public static final String DRIVER = "org.postgresql.Driver";

    private DatabaseConfig() {
        // utilitario - sem instancias
    }
}
