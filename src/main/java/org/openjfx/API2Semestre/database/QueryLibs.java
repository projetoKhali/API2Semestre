package org.openjfx.api2semestre.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import org.openjfx.api2semestre.classes.Appointment;
import org.openjfx.api2semestre.classes.AppointmentType;
import org.openjfx.api2semestre.classes.Status;

public class QueryLibs {

    /// Retorna a conexão com o banco de dados atualmente ativa.
    /// Caso não exista conexão, uma nova conexão é criada.
    private static Connection getConnection() {

        try {
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
    public static void simpleSelect (String requester) {
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
            conexao.close();
        } catch (Exception ex) {
            System.out.println("QueryLibs.simpleSelect() -- Erro ao executar query");
            ex.printStackTrace();
        }
    }

    /// Insere um apontamento no banco de dados.
    public static void insertTable (Appointment Apt) {

        Connection conexao = getConnection();

        // código sql a ser executado, passando "?" como parâmetro de valors
        String sql = "INSERT INTO apontamento (hora_inicio, hora_fim, requester, projeto, cliente, tipo, justificativa, cr_id, aprovacao) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            // substituindo os parâmetros "?" para valores desejados
            // statement.setInt(1, Apt.getId());
            // System.out.println(Apt.getStartDate());
            statement.setTimestamp(1, Apt.getStartDate());
            statement.setTimestamp(2, Apt.getEndDate());
            statement.setString(3, Apt.getRequester());
            statement.setString(4, Apt.getProject());
            statement.setString(5, Apt.getClient());
            statement.setBoolean(6, Apt.getType().getBooleanValue());
            statement.setString(7, Apt.getJustification());
            statement.setString(8, Apt.getSquad());
            statement.setInt(9, Apt.getStatus().getIntValue());

            // executa o update
            statement.executeUpdate();

            // envia mudanças para conexão remota
            conexao.commit();
            conexao.close();

            // exibe erros ao executar a query
        } catch (Exception ex) {
            System.out.println("QueryLibs.insertTable() -- Erro: Falha na execução da Query!");
            ex.printStackTrace();
        }
    }

    /// Executa um arquivo SQL no caminho especificado.
    public static void executeSqlFile (String file_path) {
        Connection conexao = getConnection();

        // Abre o arquivo e inicializa um StringBuilder para a leitura dos comandos SQL
        try {

            BufferedReader br = new BufferedReader(new FileReader(file_path));

            StringBuilder sb = new StringBuilder();
            String linha;

            // Lê a próxima linha. Repete enquanto linha não for nula
            while ((linha = br.readLine()) != null) {

                // adiciona a linha ao StringBuilder e quebra de linha ao final
                sb.append(linha).append(System.lineSeparator());
            }

            // Converte o StringBuilder em uma String
            String sql = sb.toString();

            // System.out.println("QueryLibs.executeSqlFile() -- File loaded, executing SQL commands:\n" + sql);

            // executa as instruções SQL contidas no arquivo
            Statement statement = conexao.createStatement();

            statement.execute(sql);
            // envia mudanças para conexão remota
            conexao.commit();
            conexao.close();
            br.close();
                
        } catch (Exception ex) {
            System.out.println("QueryLibs.executeSqlFile() -- Erro ao executar query para o arquivo " + file_path);
            ex.printStackTrace();
        }
        
    }

    public static Appointment[] collaboratorSelect (String requester) {

        Connection conexao = getConnection();

        // string que carrega o comando em sql
        String sql = "SELECT * FROM vw_apontamento WHERE requester = ?";
        
        var appointments = new ArrayList<Appointment>();
        
        // execução da query
        try {

            PreparedStatement statement = conexao.prepareStatement(sql);

            // substitui "?" pelo id passado no parâmetro
            statement.setString(1, requester);
            // executa a query e salva o resultado na variável "result"
            ResultSet result = statement.executeQuery();
            
            // cabeçalho
            // System.out.println("Usuário | id | hora início | hora fim | projeto | cliente | atividade | justificativa | centro resultado");
            
            while (result.next()) {
                System.out.println("oi result.next()");
                // itera sobre cada linha retornada pela consulta
                // e extrai os valores das colunas necessárias
                int id = result.getInt("apt_id");
                Timestamp hora_inicio = new Timestamp(((Date) result.getObject("hora_inicio")).getTime());
                Timestamp hora_fim = new Timestamp(((Date) result.getObject("hora_fim")).getTime());
                // String requester = result.getString("usuario_nome");
                String projeto = result.getString("projeto");
                String cliente = result.getString("cliente");
                boolean tipo = result.getBoolean("tipo");
                String justif = result.getString("justificativa");
                String centroR = result.getString("cr_id");
                int aprovacao = result.getInt("aprovacao");

                appointments.add(new Appointment(
                    id,
                    requester,
                    AppointmentType.of(tipo),
                    hora_inicio,
                    hora_fim,
                    projeto,
                    cliente,
                    justif,
                    centroR,
                    aprovacao
                ));

                // // imprime os valores das colunas no terminal
                // System.out.println(requester
                //         + " | " + id
                //         + " | " + hora_inicio
                //         + " | " + hora_fim
                //         + " | " + projeto
                //         + " | " + cliente
                //         + " | " + tipo
                //         + " | " + justif
                //         + " | " + centroR
                //         + " | " + aprovacao);
            }
            // fecha a conexão
            conexao.close();
    
        } catch (Exception ex) {
            System.out.println("QueryLibs.collaboratorSelect() -- Erro ao executar query");
            ex.printStackTrace();
        }
        return appointments.toArray(new Appointment[0]);
    }

    /// Atualiza um apontamento no banco de dados.
    public static void updateTable (Appointment Apt) {

        Connection conexao = getConnection();

        // código sql a ser executado, passando "?" como parâmetro de valors
        // No SQL abaixo, o ID do apontamento é o parâmentro para a atualização. O
        // ultimo stratement é o getId, então será necessario coletar o ID do
        // apantamento
        // para reconhecer qual apontamento será atualizado.
        String sql = "UPDATE apontamento SET hora_inicio = ?, hora_fim = ?, usr_id = ?, projeto = ?, cliente = ?, tipo = ?, cr_id = ? WHERE apt_id = ?";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {
            // substituindo os parâmetros "?" para valores desejados
            statement.setObject(1, Date.from(Apt.getStartDate().toInstant()));
            statement.setObject(2, Date.from(Apt.getEndDate().toInstant()));
            statement.setString(3, Apt.getRequester());
            statement.setString(4, Apt.getProject());
            statement.setString(5, Apt.getClient());
            statement.setBoolean(6, Apt.getType().getBooleanValue());
            statement.setString(7, Apt.getJustification());
            statement.setString(8, Apt.getSquad());
            statement.setString(9, Apt.getSquad());

            // executa o update
            statement.executeUpdate();

            conexao.commit();
            conexao.close();

            // exibe erros ao executar a query
        } catch (Exception ex) {
            System.out.println("QueryLibs.updateTable() -- Erro: Falha na execução da Query!");
            ex.printStackTrace();
        }
    }

    /// Remove um apontamento do banco de dados.
    public static void deleteIdAppointment(Appointment Apt) {

        Connection conexao = getConnection();

        // código sql a ser executado, passando "?" como parâmetro de valors
        // Como base no ID do apontamento ele exclui todas regristro dentro da condição
        // "Coluna apt_id = ID do apontamento"
        String sql = "DELETE FROM apontamento WHERE apt_id = ?";
        try (PreparedStatement statement = conexao.prepareStatement(sql)) {

            // substituindo os parâmetros "?" para valores desejados
            statement.setInt(1, Apt.getId());

            // executa o update
            statement.executeUpdate();

            conexao.commit();
            conexao.close();

            // exibe erros ao executar a query
        } catch (Exception ex) {
            System.out.println("QueryLibs.deleteIdAppointment() -- Erro: Falha na execução da Query!");
            ex.printStackTrace();
        }
    }

    public static void testConnection(Connection conexao) {
        // Connection conexao = getConnection(); // Add param to testConnection so that it doesn't call getConnection() creating infinite loop

        // cria tabela, insere dados, deleta dados e deleta tabela
        try {
            // cria tabela de teste
            String sql = "create table if not exists teste(nome varchar null);";
            try (PreparedStatement statement = conexao.prepareStatement(sql)) {
                statement.executeUpdate();
            } catch (Exception e) {
                System.out.println("QueryLibs.testConnection() -- erro: " + e);
                System.out.println("erro ao criar tabela");
            }
            // insere dado na tabela
            sql = "insert into teste(nome) values(?)";
            try (PreparedStatement statement = conexao.prepareStatement(sql)) {
                statement.setString(1, "teste de insert");
                statement.executeUpdate();
            } catch (Exception e) {
                System.out.println("QuerLibs.testConnection() --erro: " + e);
                System.out.println("erro ao inserir dado na tabela");
            }
            // faz um select na tabela
            sql = "select * from teste;";
            try (PreparedStatement statement = conexao.prepareStatement(sql)){
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    String nome = result.getString("nome");
                    System.out.println(nome);
                }
            } catch (Exception e) {
                System.out.println("QuerLibs.testConnection() --erro: " + e);
                System.out.println("erro ao pesquisar dados da tabela");
            }
            // deleta dado da tabela
            sql = "delete from teste;";
            try (PreparedStatement statement = conexao.prepareStatement(sql)) {
                statement.executeUpdate();
            } catch (Exception e) {
                System.out.println("QueryLibs.testeConnection() -- erro: " + e);
                System.out.println("erro ao deletar dado da tabela");
            }
            // deleta tabela
            sql = "drop table teste cascade;";
            try (PreparedStatement statement = conexao.prepareStatement(sql)) {
                statement.executeUpdate();
            } catch (Exception e) {
                System.out.println("QueryLibs.testeConnection() -- erro: " + e);
                System.out.println("erro ao deletar tabela");
            }

            conexao.commit();
        } catch (Exception e) {
            System.out.println("Querylibs.testConnection: " + e);
        }
        // fecha conexão
        // conexao.close();
    }
}