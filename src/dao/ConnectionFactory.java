package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import util.DatabaseConfig;

/**
 * Fabrica de conexoes JDBC. Centraliza a abertura de conexao com o
 * PostgreSQL (Supabase).
 */
public final class ConnectionFactory {

    static {
        try {
            Class.forName(DatabaseConfig.DRIVER);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(
                    "Driver JDBC do PostgreSQL nao encontrado. "
                  + "Adicione o postgresql-XX.X.X.jar ao classpath (pasta lib/).", e);
        }
    }

    private ConnectionFactory() {
        // utilitario
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DatabaseConfig.URL,
                DatabaseConfig.USUARIO,
                DatabaseConfig.SENHA);
    }
}
