package org.openjfx.API2Semestre.JavaFiles;

import java.io.IOException;
// importando pacotes para conexão sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    private static Connection conexao;

    public static Connection getConnection () {
        return conexao;
    }
    
    // método de conexão com banco
    public Connection connect() {

        // Dados para conexão com banco
        String host = "host"; // endereço do servidor
        String port = "port"; // porta de conexão do servidor
        String userName = "userName"; // nome do usuário para acesso ao banco
        String password = "password"; // senha do usuário para acesso ao banco
        String database = "database"; // nome do banco de dados a ser utilizado
        // driver de conexão
        String driver = "jdbc:postgresql://" + host + ":" + port + "/" + database;
        
        try {
            //carrega a classe do driver do PostgreSQL na memória permitindo comunicação com o banco de dados
            Class.forName("org.postgresql.Driver");

            // objeto "conexao" para execução de comandos SQL
            conexao = DriverManager.getConnection(driver, userName, password);

        // tratamento de erros
        } catch (ClassNotFoundException ex) {
            System.out.println("Driver do banco de dados não localizado!");
        } catch (SQLException ex) {
            System.out.println("Erro ao conectar com o banco de dados: " + ex.getMessage());
        } 
        return conexao;
    }

    public static void main(String[] args) throws SQLException, IOException {
        SQLConnection sqlConnection = new SQLConnection();
        Connection conexao = sqlConnection.connect();

        if (conexao != null) {
            System.out.println("Conexão feita com sucesso!");
        } else {
            System.out.println("Falha ao se conectar ao Banco de dados");
        }
        
        
        // executa arquivos sql passando o endereço do arquivo como parâmetro
        String arquivoSql = "./SQL/Tabelas.sql";
        QueryLibs.executeSqlFile(arquivoSql);

        // faz inserts na tabela
        // QueryLibs.insertTable(conexao, apt );

        // tras os apontamentos referentes ao id do usuário passado como parâmetro
        // QueryLibs.collaboratorSelect(1);
        if (conexao != null) {
            conexao.close();
        }
    }
}