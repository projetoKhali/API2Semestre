package org.openjfx.api2semestre.database;

// importando pacotes para leitura de arquivo
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// importando pacotes para conexão sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.openjfx.api2semestre.App;

public class SQLConnection {

    // método de conexão com banco
    public static Connection connect() throws IOException {

        String[] env = new String[] {
            "host",
            "port",
            "userName",
            "password",
            "database",
        };

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(App.getEnvLocation()));
            // loop through the array "env" and overwrite it's contents with the file's contents.
            // each line contains "field:value", we extract only the value by doing a split on ":" 
            // and accessing the index 1 of the result. If we reach the end of the file while trying to
            // call br.readline(), we catch the exception
            for (int i = 0; i < env.length; i++) {
                env[i] = br.readLine().split(":")[1];                
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (br != null) br.close();
        }

        // Dados para conexão com banco
        String host = env[0]; // endereço do servidor
        String port = env[1]; // porta de conexão do servidor
        String userName = env[2]; // nome do usuário para acesso ao banco
        String password = env[3]; // senha do usuário para acesso ao banco
        String database = env[4]; // nome do banco de dados a ser utilizado

        // driver de conexão
        String driver = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        Connection conexao = null;

        // tenta abrir conexão
        try {
            //carrega a classe do driver do PostgreSQL na memória permitindo comunicação com o banco de dados
            Class.forName("org.postgresql.Driver");

            // objeto "conexao" para execução de comandos SQL
            conexao = DriverManager.getConnection(driver, userName, password);
            conexao.setAutoCommit(false);

        // tratamento de erros
        } catch (ClassNotFoundException ex) {
            System.out.println("SQLConnector.connect() -- Erro: Driver do banco de dados não localizado!");
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("SQLConnector.connect() -- Erro: ao conectar com o banco de dados!");
            ex.printStackTrace();
        } 
        return conexao;

    }

}