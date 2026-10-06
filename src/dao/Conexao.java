package dao;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexao {

    private static final String ARQUIVO_CONFIG = "config.properties";

    // Os dados do banco ficam no arquivo config.properties (na pasta do projeto),
    // assim a senha nao fica escrita no codigo nem vai para o GitHub.
    public static Connection conectar() throws SQLException {
        Properties config = new Properties();

        try (FileInputStream arquivo = new FileInputStream(ARQUIVO_CONFIG)) {
            config.load(arquivo);
        } catch (IOException e) {
            throw new SQLException("Nao foi possivel ler o arquivo " + ARQUIVO_CONFIG, e);
        }

        return DriverManager.getConnection(
                config.getProperty("db.url"),
                config.getProperty("db.usuario"),
                config.getProperty("db.senha"));
    }
}
