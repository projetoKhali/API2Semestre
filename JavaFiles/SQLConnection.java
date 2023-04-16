import java.io.IOException;
// importando pacotes para conexão sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLConnection {

    static Connection conexao;
    
    // método de conexão com banco
    public Connection connect(){

        // Dados para conexão com banco
        String host = "localhost"; // endereço do servidor
        String port = "5432"; // porta de conexão do servidor
        String userName = "postgres"; // nome do usuário para acesso ao banco
        String password = "Postgres!1@2#3"; // senha do usuário para acesso ao banco
        String database = "Khali"; // nome do banco de dados a ser utilizado
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
        // SQLConnection sqlConnection = new SQLConnection();
        // Connection conexao = sqlConnection.connect();
        
        // executa arquivos sql passando o endereço do arquivo como parâmetro
        // String arquivoSql = "./SQL/Tabelas.sql";
        // QueryLibs.executeSqlFile(conexao, arquivoSql);

        // faz inserts na tabela
        // QueryLibs.insertTable(conexao);

        // tras os apontamentos referentes ao id do usuário passado como parâmetro
        QueryLibs.collaboratorSelect(1);
        conexao.close();
    }
}