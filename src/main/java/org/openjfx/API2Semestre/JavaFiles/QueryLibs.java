package org.openjfx.API2Semestre.JavaFiles;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Statement;
import java.util.Date;

import org.openjfx.API2Semestre.Classes.Appointment;
import org.openjfx.API2Semestre.Classes.AppointmentType;

public class QueryLibs {

    /// Retorna a conexão com o banco de dados atualmente ativa.
    /// Caso não exista conexão, uma nova conexão é criada.
    private static Connection getConnection () {
        
        // Chama SQLConnection.getConnection() para retornar a conexão caso haja uma salva atualmente
        Connection conexao = SQLConnection.getConnection();

        // Se a conexão não for nula, retorna
        if (conexao != null) {
            return conexao;
        
        // Não existe uma conexão previamente estabelecida, cria uma nova e retorna
        } try {
            return new SQLConnection().connect();
        
        // Em caso de erro ao estabelecer uma nova conexão
        } catch (Exception ex) {
            // throw(e);

            System.out.println("QueryLibs.getConnection() -- Erro: Falha ao iniciar conexão!");
            ex.printStackTrace();
            return null;
        }

    }

    /// Método que executa um select simples dos apontametos de um usuário.
    /// Pode lançar uma exceção SQLException.
    public static void simpleSelect (String requester) throws SQLException {
        Connection conexao = getConnection();

        // string que carrega o comando em sql
        String sql = "SELECT * FROM apontamento WHERE requester = ?";

        // execução da query
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            statement.setString(1, requester);
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
                System.out.println(coluna1 + " | " + coluna2 + " | " + coluna3);
            }
        }
    }

    /// Insere um apontamento no banco de dados.
    public static void insertTable (Appointment Apt) throws SQLException {

        Connection conexao = getConnection();

        // código sql a ser executado, passando "?" como parâmetro de valors
        String sql = "INSERT INTO apontamento (hora_inicio, hora_fim, requester, projeto, cliente, tipo, justificativa, cr_id) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            // substituindo os parâmetros "?" para valores desejados
            // statement.setInt(1, Apt.getId());
            // System.out.println(Apt.getStartDate());
            statement.setTimestamp(1, Apt.getStartDate());
            statement.setTimestamp(2, Apt.getEndDate());
            statement.setString(3, Apt.getRequester());
            statement.setString(4, Apt.getProject());
            statement.setString(5, Apt.getClient());
            statement.setBoolean(6, Apt.getType() == AppointmentType.Overtime);
            statement.setString(7, Apt.getJustification());
            statement.setString(8, Apt.getSquad());

            // executa o update
            statement.executeUpdate();

        // exibe erros ao executar a query
        } catch (Exception ex) {
            System.out.println("QueryLibs.insertTable() -- Erro: Falha na execução da Query!");
            ex.printStackTrace();
        }
    }

    /// Executa um arquivo SQL no caminho especificado.
    public static void executeSqlFile (String file_path) throws SQLException, IOException {
        Connection conexao = getConnection();

        // Abre o arquivo e inicializa um StringBuilder para a leitura dos comandos SQL
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            StringBuilder sb = new StringBuilder();
            String linha;

            // Lê a próxima linha. Repete enquanto linha não for nula
            while ((linha = br.readLine()) != null) {

                // adiciona a linha ao StringBuilder e quebra de linha ao final
                sb.append(linha).append(System.lineSeparator());
            }

            // Converte o StringBuilder em uma String
            String sql = sb.toString();
            
            // executa as instruções SQL contidas no arquivo
            try (Statement statement = conexao.createStatement()) {
                statement.execute(sql);
            }
        }
    }

    public static void collaboratorSelect (int usuario_id) throws SQLException, IOException {
        
        Connection conexao = getConnection();

        // string que carrega o comando em sql
        String sql = "SELECT * FROM vw_apontamento WHERE requester = ?";

        // execução da query
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {

            // substitui "?" pelo id passado no parâmetro
            statement.setInt(1, usuario_id);
            // executa a query e salva o resultado na variável "result"
            ResultSet result = statement.executeQuery();

            // cabeçalho
            System.out.println("Usuário | hora início | hora fim | projeto | cliente | atividade | justificativa | centro resultado");

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
                System.out.println(usuario
                    + " | " + hora_inicio
                    + " | " + hora_fim
                    + " | " + projeto
                    + " | " + cliente
                    + " | " + tipo
                    + " | " + justif
                    + " | " + centroR
                );
            }
        }
    }

    /// Atualiza um apontamento no banco de dados.
    public static void updateTable (Appointment Apt) throws SQLException {

        Connection conexao = getConnection();

        // código sql a ser executado, passando "?" como parâmetro de valors
        // No SQL abaixo, o ID do apontamento é o parâmentro para a atualização. O ultimo stratement é o getId, então será necessario coletar o ID do apantamento
        //para reconhecer qual apontamento será atualizado.
        String sql = "UPDATE apontamento SET hora_inicio = ?, hora_fim = ?, usr_id = ?, projeto = ?, cliente = ?, tipo = ?, cr_id = ? WHERE apt_id = ?";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            // substituindo os parâmetros "?" para valores desejados
            statement.setObject(1, Date.from(Apt.getStartDate().toInstant()));
            statement.setObject(2, Date.from(Apt.getEndDate().toInstant()));
            statement.setString(3, Apt.getRequester());
            statement.setString(4, Apt.getProject());
            statement.setString(5, Apt.getClient());
            statement.setString(6, Apt.getType().toString());
            statement.setString(7, Apt.getJustification());
            statement.setString(8, Apt.getSquad());
            statement.setInt(9, Apt.getId());

            // executa o update
            statement.executeUpdate();

        // exibe erros ao executar a query
        } catch (Exception ex) {
            System.out.println("QueryLibs.updateTable() -- Erro: Falha na execução da Query!");
            ex.printStackTrace();
        }
    }

    /// Remove um apontamento do banco de dados.
    public static void deleteIdAppointment (Appointment Apt) throws SQLException {

        Connection conexao = getConnection();

        // código sql a ser executado, passando "?" como parâmetro de valors
        // Como base no ID do apontamento ele exclui todas regristro dentro da condição "Coluna apt_id = ID do apontamento"
        String sql = "DELETE FROM apontamento WHERE apt_id = ?";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {

            // substituindo os parâmetros "?" para valores desejados
            statement.setInt(1, Apt.getId());

            // executa o update
            statement.executeUpdate();

        // exibe erros ao executar a query
        } catch (Exception ex) {
            System.out.println("QueryLibs.deleteIdAppointment() -- Erro: Falha na execução da Query!");
            ex.printStackTrace();
        }
    }
}