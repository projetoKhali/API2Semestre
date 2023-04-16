import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Statement;

public class QueryLibs {

    // método de selec simples
    public static void simpleSelect() throws SQLException {
        // declara variável conexão fora do bloco try
        Connection conexao = null;
        
        // tenta iniciar conexão com o Banco
        try {
            SQLConnection sqlConnection = new SQLConnection();
            conexao = sqlConnection.connect();            
        } catch (Exception e) {
            // exibe erros ao iniciar conexão caso haja
            System.out.println("Falha ao iniciar conexão: " + e);
        }
        

        // string que carrega o comando em sql
        String sql = "SELECT * FROM tabela";

        // execução da query
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            // prepara a declaração SQL para ser executada usando a conexão fornecida
            // e executa a consulta
            ResultSet result = statement.executeQuery();

            // processa o resultado aqui...
            while (result.next()) {
                // itera sobre cada linha retornada pela consulta
                // e extrai os valores das colunas necessárias
                String coluna1 = result.getString("nome_da_coluna_1");
                int coluna2 = result.getInt("nome_da_coluna_2");
                double coluna3 = result.getDouble("nome_da_coluna_3");

                // imprime os valores das colunas no terminal
                System.out.println(coluna1 + " - " + coluna2 + " - " + coluna3);
            }
        }

        // tenta finalizar a conexão com banco
        try {
            conexao.close();
        } catch (Exception e) {
            // descreve excesão
            System.out.println("Falha ao encerrar conexão: " + e);
        }
    }

    public static void insertTable() throws SQLException {
        // declara variável conexão fora do bloco try
        Connection conexao = null;
        
        // tenta iniciar conexão com o Banco
        try {
            SQLConnection sqlConnection = new SQLConnection();
            conexao = sqlConnection.connect();            
        } catch (Exception e) {
            // exibe erros ao iniciar conexão caso haja
            System.out.println("Falha ao iniciar conexão: " + e);
        }


        // código sql a ser executado, passando "?" como parâmetro de valors
        String sql = "INSERT INTO tabela_teste (nome, nome2) values (?, ?)";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            // substituindo os parâmetros "?" para valores desejados
            statement.setString(1, "jhow");
            statement.setString(2, "nicole");
            // executa o update
            statement.executeUpdate();
            // exibe erros ao executar a query
        } catch (Exception ex) {
            System.out.println("Erro ao executar a query: " + ex.getMessage());
        }

        // tenta fechar a conexão
        try {
            conexao.close();
        } catch (Exception ex) {
            // exibe erro ao tentar fechar conexão
            System.out.println("Falha ao fechar conexão: " + ex);
        } 
    }

    public static void executeSqlFile(String arquivoSql) throws SQLException, IOException {
        // declara variável conexão fora do bloco try
        Connection conexao = null;
        
        // tenta iniciar conexão com o Banco
        try {
            SQLConnection sqlConnection = new SQLConnection();
            conexao = sqlConnection.connect();            
        } catch (Exception e) {
            // exibe erros ao iniciar conexão caso haja
            System.out.println("Falha ao iniciar conexão: " + e);
        }
        
        // tenta executar a query
        try (BufferedReader br = new BufferedReader(new FileReader(arquivoSql))) {
            String linha;
            StringBuilder sb = new StringBuilder();
            while ((linha = br.readLine()) != null) {
                // adiciona a linha ao StringBuilder
                sb.append(linha);
                sb.append(System.lineSeparator()); // Adiciona quebra de linha ao final de cada linha lida
            }
            String sql = sb.toString(); // Converte o StringBuilder em uma String
            try (Statement statement = conexao.createStatement()) {
                // executa as instruções SQL contidas no arquivo
                statement.execute(sql);
            }
        }

        // tenta fechar conexão
        try {
            conexao.close();
        } catch (Exception ex) {
            System.out.println("Falha ao fechar conexão: " + ex);
        }
    }

    public static void collaboratorSelect(int usuario_id) throws SQLException, IOException {
        // declara variável conexão fora do bloco try
        Connection conexao = null;
        
        // tenta iniciar conexão com o Banco
        try {
            SQLConnection sqlConnection = new SQLConnection();
            conexao = sqlConnection.connect();            
        } catch (Exception e) {
            // exibe erros ao iniciar conexão caso haja
            System.out.println("Falha ao iniciar conexão: " + e);
        }

        // string que carrega o comando em sql
        String sql = "SELECT * FROM vw_apontamento WHERE usr_id = ?";

        // execução da query
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            // substitui "?" pelo id passado no parâmetro
            statement.setInt(1, usuario_id);
            // executa a query e salva o resultado na variável "result"
            ResultSet result = statement.executeQuery();

            System.out.println(result);

            // verifica se a consulta retornou resultados
            // caso não tenha, exibe mensagem de erro e sai do método
            // caso contrário, imprime os resultados na tela
            // e itera sobre os resultados, extraindo os valores das colunas necessárias
            // e imprime os valores das colunas no terminal
            
            if (!result.next()) {
                System.out.println("Nenhum resultado encontrado");
                return;
            } 

            // cabeçalho
            System.out.println(
                    "Usuário - hora início - hora fim - projeto - cliente - atividade - justificativa - centro resultado");
            while (result.next()) {
                // itera sobre cada linha retornada pela consulta
                // e extrai os valores das colunas necessárias
                String hora_inicio = result.getString("hora_inicio");
                String hora_fim = result.getString("hora_fim");
                String usuario = result.getString("usuario_nome");
                String projeto = result.getString("projeto");
                String cliente = result.getString("cliente");
                String tipo = result.getString("tipo");
                String justif = result.getString("justificativa");
                String centroR = result.getString("cr_nome");

                // imprime os valores das colunas no terminal
                System.out.println(usuario + " - " + hora_inicio + " - " + hora_fim + " - " + projeto + " - " + cliente
                        + " - " + tipo + " - " + justif + " - " + centroR);
            }
        }

        // finaliza conexão com o Banco
        try {
            conexao.close();
        } catch (Exception ex) {
            // exibe erro ao fechar conexão, caso haja
            System.out.println("Falha ao finalizar conexão: " + ex);
            return;
        }
    }
}