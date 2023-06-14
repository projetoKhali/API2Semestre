package org.openjfx.api2semestre.database;

// importando pacotes para leitura de arquivo
import java.io.BufferedReader;
import java.io.FileReader;

// importando pacotes para conexão sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.openjfx.api2semestre.App;

public class SQLConnection {

    // método de conexão com banco
    public static Connection connect() {

        // Dados para conexão com banco
        String[] env = new String[] {
            "host", // endereço do servidor
            "port", // porta de conexão do servidor
            "userName", // nome do usuário para acesso ao banco
            "password", // senha do usuário para acesso ao banco
            "database", // nome do banco de dados a ser utilizado
        };

        // tenta abrir conexão
        try (BufferedReader br = new BufferedReader(new FileReader(App.getEnvLocation()))) {
            // loop through the array "env" and overwrite it's contents with the file's contents.
            // each line contains "field:value", we extract only the value by doing a split on ":" 
            // and accessing the index 1 of the result. If we reach the end of the file while trying to
            // call br.readline(), we catch the exception
            for (int i = 0; i < env.length; i++) env[i] = br.readLine().split(":")[1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            // carrega a classe do driver do PostgreSQL na memória permitindo comunicação com o banco de dados
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("SQLConnector.connect() -- Erro: Driver do banco de dados não localizado!");
            ex.printStackTrace();
        }
        try {
            // objeto "conexao" para execução de comandos SQL
            Connection connection = DriverManager.getConnection(
                "jdbc:postgresql://" + env[0] + ":" + env[1] + "/" + env[4], env[2], env[3]
            );
            connection.setAutoCommit(false);
            return connection;

        // tratamento de erros
        } catch (SQLException ex) {
            System.out.println("SQLConnector.connect() -- Erro ao conectar com o banco de dados!");
            ex.printStackTrace();
        }
        return null;
    }

}