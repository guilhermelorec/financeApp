package util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Centraliza as configuracoes de acesso ao banco de dados PostgreSQL (Supabase).
 *
 * As credenciais sao lidas do arquivo "config.properties" (na raiz do projeto).
 * Esse arquivo esta no .gitignore - copie o "config.properties.example" para
 * "config.properties" e preencha com os dados do seu projeto Supabase.
 */
public final class DatabaseConfig {

    public static final String DRIVER = "org.postgresql.Driver";

    public static final String HOST;
    public static final int    PORTA;
    public static final String DATABASE;
    public static final String USUARIO;
    public static final String SENHA;
    public static final String URL;

    static {
        Properties p = carregar();

        HOST     = require(p, "db.host");
        PORTA    = Integer.parseInt(p.getProperty("db.porta", "5432").trim());
        DATABASE = p.getProperty("db.database", "postgres").trim();
        USUARIO  = require(p, "db.usuario");
        SENHA    = require(p, "db.senha");

        URL = "jdbc:postgresql://" + HOST + ":" + PORTA + "/" + DATABASE
            + "?sslmode=require";
    }

    private DatabaseConfig() {
        // utilitario - sem instancias
    }

    private static Properties carregar() {
        Properties props = new Properties();

        // 1. Tenta ler do classpath (com.exemplo.../bin/config.properties)
        try (InputStream in = DatabaseConfig.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
                return props;
            }
        } catch (IOException ignored) {
            // tenta proxima estrategia
        }

        // 2. Tenta ler do diretorio atual de execucao
        Path arquivo = Path.of("config.properties");
        if (Files.exists(arquivo)) {
            try (InputStream in = Files.newInputStream(arquivo)) {
                props.load(in);
                return props;
            } catch (IOException e) {
                throw new RuntimeException(
                        "Erro ao ler config.properties: " + e.getMessage(), e);
            }
        }

        throw new RuntimeException(
                "Arquivo 'config.properties' nao encontrado. "
              + "Copie 'config.properties.example' para 'config.properties' "
              + "e preencha com as credenciais do Supabase.");
    }

    private static String require(Properties p, String chave) {
        String valor = p.getProperty(chave);
        if (valor == null || valor.trim().isEmpty()) {
            throw new RuntimeException(
                    "Propriedade obrigatoria ausente em config.properties: " + chave);
        }
        return valor.trim();
    }
}
