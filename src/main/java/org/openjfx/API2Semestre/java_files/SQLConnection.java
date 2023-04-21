package org.openjfx.api2semestre.java_files;

// importando pacotes para leitura de arquivo
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

// importando pacotes para conexão sql
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
// import java.time.LocalDate;

// import org.openjfx.api2semestre.DateConverter;
// import org.openjfx.api2semestre.classes.Appointment;
// import org.openjfx.api2semestre.classes.AppointmentType;

public class SQLConnection {

    private static Connection conexao;

    public static Connection getConnection () {
        return conexao;
    }
    
    // método de conexão com banco
    public Connection connect() throws IOException {

        String[] env = new String[] {
            "host",
            "port",
            "userName",
            "password",
            "database",
        };

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("./.env"));
            
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
        
        try {
            //carrega a classe do driver do PostgreSQL na memória permitindo comunicação com o banco de dados
            Class.forName("org.postgresql.Driver");

            // objeto "conexao" para execução de comandos SQL
            conexao = DriverManager.getConnection(driver, userName, password);

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

    // public static void main(String[] args) throws SQLException, IOException {
    //     // SQLConnection sqlConnection = new SQLConnection();
    //     // Connection conexao = sqlConnection.connect();

    //     // if (conexao != null) {
    //     //     System.out.println("Conexão feita com sucesso!");
    //     // } else {
    //     //     System.out.println("Falha ao se conectar ao Banco de dados");
    //     // }
        
        
    //     // executa arquivos sql passando o endereço do arquivo como parâmetro
    //     // QueryLibs.executeSqlFile("./SQL/Tabelas.sql");

    //     // teste
    //     Appointment apt = new Appointment(
    //         "testemtloko",
    //         AppointmentType.Overtime,
    //         DateConverter.inputToTimestamp(LocalDate.of(2013, 12, 1), "11:00"),
    //         DateConverter.inputToTimestamp(LocalDate.of(2013, 12, 1), "12:00"),
    //         "squadx",
    //         "clienteteste",
    //         "projetoteste",
    //         "tinha uns trampo"
    //     );

    //     // Erro ao executar a query: ERROR: column "requester" of relation "apontamento" does not exist
    //     //      Posição: 49
    //     // Erro ao executar a query: ERROR: column "usr_id" is of type integer but expression is of type character varying
    //     //      Dica: You will need to rewrite or cast the expression.
    //     //      Posição: 119
    //     QueryLibs.insertTable(apt);
    //     QueryLibs.simpleSelect("testemtloko");

    //     // tras os apontamentos referentes ao id do usuário passado como parâmetro
    //     // QueryLibs.collaboratorSelect(1);
    //     if (conexao != null) {
    //         conexao.close();
    //     }
    // }
}