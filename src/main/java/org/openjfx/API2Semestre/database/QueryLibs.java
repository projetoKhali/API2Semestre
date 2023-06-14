package org.openjfx.api2semestre.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.data.Client;
import org.openjfx.api2semestre.data.MemberRelation;
import org.openjfx.api2semestre.data.ResultCenter;
import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.database.query.Query;
import org.openjfx.api2semestre.database.query.QueryParam;
import org.openjfx.api2semestre.database.query.QueryTable;
import org.openjfx.api2semestre.database.query.QueryType;
import org.openjfx.api2semestre.database.query.TableProperty;
import org.openjfx.api2semestre.utils.PasswordIncription;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class QueryLibs {

/// -----------------------------------------------------------------------------------------------------------------------
/// --------------------------------- MANIPULAÇÃO DE CONEXÃO --------------------------------------------------------------
/// -----------------------------------------------------------------------------------------------------------------------

    public static Optional<Connection> connect () {
        try {
            return Optional.ofNullable(SQLConnection.connect());
        } catch (Exception ex) {
            System.out.println("QueryLibs.connect() -- Erro: Falha ao iniciar conexão!");
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public static boolean close (Optional<Connection> connectionOptional) {
        try {
            return close(connectionOptional.orElseThrow(
                () -> new Exception("QueryLibs.close() -- Erro: Connexão inválida")
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean close (Connection connection) throws Exception {
        try {
            connection.commit();
            connection.close();
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

/// -----------------------------------------------------------------------------------------------------------------------
/// --------------------------------- COMANDOS GENÉRICOS BÁSICOS ----------------------------------------------------------
/// -----------------------------------------------------------------------------------------------------------------------

    private static Optional<ResultSet> executeQuery (Query q, Optional<Connection> connectionOptional) {
        try {
            ObjectProperty<Boolean> commitAndClose = new SimpleObjectProperty<Boolean>(false);
            Connection connection = connectionOptional.orElseGet(() -> {
                commitAndClose.set(true);
                return SQLConnection.connect();
            });
            Optional<ResultSet> result = q.execute(connection);    
            if (commitAndClose.get()) close(connection);
            return result;
        } catch (Exception ex) {
            System.out.println("QueryLibs.executeQuery() -- Erro: Falha na execução da Query!");
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    /// Executa um arquivo SQL no caminho especificado.
    public static void executeSqlFile (String file_path, Optional<Connection> connectionOptional) {

        // Abre o arquivo e inicializa um StringBuilder para a leitura dos comandos SQL
        try (BufferedReader br = new BufferedReader(new FileReader(file_path))) {
            ObjectProperty<Boolean> commitAndClose = new SimpleObjectProperty<Boolean>(false);
            Connection connection = connectionOptional.orElseGet(() -> {
                commitAndClose.set(true);
                return SQLConnection.connect();
            });

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
            connection.createStatement().execute(sql);

            // envia mudanças para conexão remota
            if (commitAndClose.get()) close(connection);
            br.close();

        } catch (Exception ex) {
            System.out.println("QueryLibs.executeSqlFile() -- Erro ao executar query para o arquivo " + file_path);
            ex.printStackTrace();
        }
    }

/// -----------------------------------------------------------------------------------------------------------------------
/// --------------------------------- FUNÇÕES DE INSERT -------------------------------------------------------------------
/// -----------------------------------------------------------------------------------------------------------------------

    /// Executa um INSERT na tabela especificada
    private static int executeInsert (QueryTable table, QueryParam<?>[] params, Optional<Connection> connectionOptional) {
        try {
            ResultSet resultSet = executeQuery(
                new Query(
                    QueryType.INSERT,
                    table,
                    params
                ),
                connectionOptional
            ).get();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("QueryLibs.executeInsert() -- Erro: nenhum id retornado");
        return -1;
    }

    public static int insertAppointment (Appointment apt) { return insertAppointment (apt, Optional.empty()); }
    public static int insertAppointment (Appointment apt, Optional<Connection> connectionOptional) {
        return executeInsert(
            QueryTable.Appointment,
            new QueryParam<?>[] {

                new QueryParam<Timestamp> (TableProperty.Apt_StartDate, apt.getStart()),
                new QueryParam<Timestamp> (TableProperty.Apt_EndDate, apt.getEnd()),
                new QueryParam<Integer>   (TableProperty.UserId, apt.getRequester()),
                new QueryParam<String>    (TableProperty.Apt_Project, apt.getProject()),
                new QueryParam<Integer>   (TableProperty.Apt_ClientId, apt.getClientId()),
                new QueryParam<Boolean>   (TableProperty.Apt_Type, apt.getType().getBooleanValue()),
                new QueryParam<String>    (TableProperty.Apt_Justification, apt.getJustification()),
                new QueryParam<Integer>   (TableProperty.ResultCenterId, apt.getResultCenterId()),
            },
            connectionOptional
        );
    }

    // Insere uma senha criptografada no banco
    public static int insertUser (User user) { return insertUser (user, Optional.empty()); }
    public static int insertUser (User user, Optional<Connection> connectionOptional) {
        try {
            return executeInsert(
                QueryTable.User,
                new QueryParam<?> [] {
                    new QueryParam<>(TableProperty.Name, user.getName()),
                    new QueryParam<>(TableProperty.Usr_Email, user.getEmail()),
                    new QueryParam<>(TableProperty.Usr_Password, PasswordIncription.encryptPassword(user.getPassword())),     // incripta senha
                    new QueryParam<Integer>(TableProperty.Usr_Profile, user.getProfile().getProfileLevel()),
                    new QueryParam<>(TableProperty.Usr_Registration, user.getRegistration())
                },
                connectionOptional
            );
        } catch (Exception e) {
            System.out.println("ERROR: duplicate key value violates unique constraint\nEmail já existente!");
            e.printStackTrace();
            return -1;
        }
    }

    public static int insertResultCenter (ResultCenter rc) { return insertResultCenter (rc, Optional.empty()); }
    public static int insertResultCenter (ResultCenter rc, Optional<Connection> connectionOptional) {
        return executeInsert(
            QueryTable.ResultCenter,
            new QueryParam<?>[] {
                new QueryParam<String>(TableProperty.Name, rc.getName()),
                new QueryParam<String>(TableProperty.CR_Sigla, rc.getAcronym()),
                new QueryParam<String>(TableProperty.CR_Codigo, rc.getCode()),
                new QueryParam<Integer>(TableProperty.UserId, rc.getManagerId())
            },
            connectionOptional
        );
    }

    public static int addUserToResultCenter (int usr_id, int cr_id) { return addUserToResultCenter (usr_id, cr_id, Optional.empty()); }
    public static int addUserToResultCenter (int usr_id, int cr_id, Optional<Connection> connectionOptional) {
        return executeInsert(
            QueryTable.Member,
            new QueryParam<?>[] {
                new QueryParam<Integer>(TableProperty.UserId, usr_id),
                new QueryParam<Integer>(TableProperty.ResultCenterId, cr_id),
            },
            connectionOptional
        );
    }

    public static int insertClient (Client cliente) { return insertClient (cliente, Optional.empty()); }
    public static int insertClient (Client cliente, Optional<Connection> connectionOptional) {
        return executeInsert(
            QueryTable.Client,
            new QueryParam<?>[] {
                new QueryParam<String>(TableProperty.Clt_RazaoSocial, cliente.getRazaoSocial()),
                new QueryParam<String>(TableProperty.Clt_CNPJ, cliente.getCNPJ()),
            },
            connectionOptional
        );
    }

/// -----------------------------------------------------------------------------------------------------------------------
/// --------------------------------- FUNÇÕES DE SELECT -------------------------------------------------------------------
/// -----------------------------------------------------------------------------------------------------------------------

    /// Executa um SELECT especificando tipo de dado esperado, tabela e parametros da query
    /// Pode ser usada uma lista de parametros, o ultimo parametro apresentado representa o WHERE dá query.
    @SuppressWarnings("unchecked") private static <T extends Data> T[] executeSelect (
        Class<T> type,
        QueryTable table,
        QueryParam<?>[] params,
        Optional<Connection> connectionOptional
    ) {

        ResultSet result = null;
        try {
            result = executeQuery(
                new Query(
                    QueryType.SELECT,
                    table,
                    params
                ),
                connectionOptional
            ).get();
        } catch (Exception ex) {
            System.out.println("QueryLibs.executeSelect() -- Erro ao executar query");
            ex.printStackTrace();
        }
        if (result == null) {
            System.out.println("QueryLibs.executeSelect() -- Erro: Nenhum ResultSet retornado para a query");
            return (T[])new Data[0];
        }
        List<T> resultList = new ArrayList<>();
        // itera sobre cada linha retornada pela consulta
        // e extrai os valores das colunas necessárias
        try {
            if (result.isBeforeFirst()) {
                while (result.next()) {
                    resultList.add(
                        (T)Data.<T>create(type, result)
                        .orElseThrow(() -> new Exception("QueryLibs.executeSelect() -- Erro ao criar dado do tipo " + type + " através de Data.create"))
                    );
                }
            }
        } catch (Exception ex) {
            System.out.println("QueryLibs.executeSelect() -- Erro ao ler resultado da query");
            // ex.printStackTrace();
        }
        return resultList.toArray((T[])Array.newInstance(type, resultList.size()));
    }

    // select que trás apenas 1 item
    @SuppressWarnings("unchecked") private static <T extends Data> Optional<T> executeSelectOne (
        Class<T> type,
        QueryTable table,
        QueryParam<?>[] params,
        Optional<Connection> connectionOptional
    ) {
        ResultSet result = null;
        try {
            result = executeQuery(
                new Query(
                    QueryType.SELECT,
                    table,
                    params
                ),
                connectionOptional
            ).get();
        } catch (Exception ex) {
            System.out.println("QueryLibs.executeSelectOne() -- Erro ao executar query");
            ex.printStackTrace();
        }
        if (result == null) {
            System.out.println("QueryLibs.executeSelectOne() -- Erro: Nenhum ResultSet retornado para a query");
            return Optional.empty();
        }
        // itera sobre cada linha retornada pela consulta
        // e extrai os valores das colunas necessárias
        try {
            if (result.next()) return Optional.of(
                (T)Data.<T>create(type, result)
                .orElseThrow(() -> new Exception("QueryLibs.executeSelectOne() -- Erro ao criar dado do tipo " + type + " através de Data.create"))
            );
            return Optional.empty();
        } catch (Exception ex) {
            System.out.println("QueryLibs.executeSelectOne() -- Erro ao ler resultado da query");
            ex.printStackTrace();
        }
        return Optional.empty();

    }

    public static Appointment[] selectAppointmentsOfUser (int usr_id) { return selectAppointmentsOfUser (usr_id, Optional.empty()); }
    public static Appointment[] selectAppointmentsOfUser (int usr_id, Optional<Connection> connectionOptional) {
        return QueryLibs.<Appointment>executeSelect(
            Appointment.class,
            QueryTable.ViewAppointment,
            new QueryParam<?>[] {
                new QueryParam<Integer>(TableProperty.UserId, usr_id),
            },
            connectionOptional
        );
    }

    public static Optional<User> selectUserById (int id) { return selectUserById (id, Optional.empty()); }
    public static Optional<User> selectUserById (int id, Optional<Connection> connectionOptional) {
        return executeSelectOne(
            User.class,
            QueryTable.User,
            new QueryParam<?>[] {
                new QueryParam<Integer>(TableProperty.Id, id),
            },
            connectionOptional
        );
    }
    
    
    public static Optional<Appointment> selectAppointmentById (int id) { return selectAppointmentById (id, Optional.empty()); }
    public static Optional<Appointment> selectAppointmentById (int id, Optional<Connection> connectionOptional) {
        return executeSelectOne(
            Appointment.class,
            QueryTable.ViewAppointment,
            new QueryParam<?>[] {
                new QueryParam<Integer>(TableProperty.Id, id),
            },
            connectionOptional
        );
    }
    
    public static Optional<ResultCenter> selectResultCenter (int id) { return selectResultCenter (id, Optional.empty()); }
    public static Optional<ResultCenter> selectResultCenter (int id, Optional<Connection> connectionOptional) {
        return QueryLibs.<ResultCenter>executeSelectOne(
            ResultCenter.class,
            QueryTable.ViewResultCenter,
            new QueryParam<?>[] {
                new QueryParam<>(TableProperty.Id, id)
            },
            connectionOptional
        );
    }

    public static ResultCenter[] selectResultCentersManagedBy (int usr_id) { return selectResultCentersManagedBy (usr_id, Optional.empty()); }
    public static ResultCenter[] selectResultCentersManagedBy (int usr_id, Optional<Connection> connectionOptional) {
        return QueryLibs.<ResultCenter>executeSelect(
            ResultCenter.class,
            QueryTable.ViewResultCenter,
            new QueryParam<?>[] {
                new QueryParam<>(TableProperty.UserId, usr_id)
            },
            connectionOptional
        );
    }

    public static ResultCenter[] selectResultCentersOfMember (int usr_id) { return selectResultCentersOfMember (usr_id, Optional.empty()); }
    public static ResultCenter[] selectResultCentersOfMember (int usr_id, Optional<Connection> connectionOptional) {
        return Arrays.asList(QueryLibs.<MemberRelation>executeSelect(
            MemberRelation.class,
            QueryTable.Member,
            new QueryParam<?>[] {
                new QueryParam<>(TableProperty.UserId,  usr_id)
            },
            connectionOptional
        ))
        .stream()
        .map((MemberRelation relation) -> selectResultCenter (
            relation.getResultCenterId(),
            connectionOptional
        ).orElse(null))
        .filter((ResultCenter resultCenter) -> resultCenter != null)
        .collect(Collectors.toList())
        .toArray(ResultCenter[]::new);
    }

    public static User[] selectMembersOfResultCenter (int cr_id) { return selectMembersOfResultCenter (cr_id, Optional.empty()); }
    public static User[] selectMembersOfResultCenter (int cr_id, Optional<Connection> connectionOptional) {
        return Arrays.asList(QueryLibs.<MemberRelation>executeSelect(
            MemberRelation.class,
            QueryTable.Member,
            new QueryParam<?>[] {
                new QueryParam<>(TableProperty.ResultCenterId, cr_id)
            },
            connectionOptional
        ))
        .stream()
        .map((MemberRelation relation) -> selectUserById (
            relation.getUserId(),
            connectionOptional
        ).get())
        .collect(Collectors.toList())
        .toArray(User[]::new);
    }

    public static Appointment[] selectAppointmentsOfResultCenter (int cr_id) { return selectAppointmentsOfResultCenter (cr_id, Optional.empty()); }
    public static Appointment[] selectAppointmentsOfResultCenter (int cr_id, Optional<Connection> connectionOptional) {
        return QueryLibs.<Appointment>executeSelect(
            Appointment.class,
            QueryTable.ViewAppointment,
            new QueryParam<?>[] {
                new QueryParam<Integer>(TableProperty.ResultCenterId, cr_id),
            },
            connectionOptional
        );
    }

/// -----------------------------------------------------------------------------------------------------------------------
/// --------------------------------- FUNÇÕES DE SELECT ALL ---------------------------------------------------------------
/// -----------------------------------------------------------------------------------------------------------------------

    /// Executa um SELECT sem WHERE especificando o tipo de dado esperado e tabela.
    private static <T extends Data> T[] executeSelectAll (
        Class<T> type,
        QueryTable table,
        Optional<Connection> connectionOptional
    ) {

        return QueryLibs.<T>executeSelect(
            type,
            table,
            new QueryParam<?>[0],
            connectionOptional
        );
    }

    public static Appointment[] selectAllAppointments () { return selectAllAppointments (Optional.empty()); }
    public static Appointment[] selectAllAppointments (Optional<Connection> connectionOptional) {
        return QueryLibs.<Appointment>executeSelectAll(
            Appointment.class,
            QueryTable.ViewAppointment,
            connectionOptional
        );
    }

    public static User[] selectAllUsers () { return selectAllUsers (Optional.empty()); }
    public static User[] selectAllUsers (Optional<Connection> connectionOptional) {
        return QueryLibs.<User>executeSelectAll(
            User.class,
            QueryTable.User,
            connectionOptional
        );
    }

    public static User[] selectAllManagersAndAdms () { return selectAllManagersAndAdms (Optional.empty()); }
    public static User[] selectAllManagersAndAdms (Optional<Connection> connectionOptional) {
        LinkedList<User> users = new LinkedList<>(Arrays.asList(
            QueryLibs.<User>executeSelect(
                User.class,
                QueryTable.User,
                new QueryParam<?>[] {
                    new QueryParam<>(TableProperty.Usr_Profile, 1)
                },
                connectionOptional
            )
        ));
        users.addAll(Arrays.asList(
            QueryLibs.<User>executeSelect(
                User.class,
                QueryTable.User,
                new QueryParam<?>[] {
                    new QueryParam<>(TableProperty.Usr_Profile, 2)
                },
                connectionOptional
            )
        ));
        return users.toArray(User[]::new);
    }

    public static ResultCenter[] selectAllResultCenters () { return selectAllResultCenters (Optional.empty()); }
    public static ResultCenter[] selectAllResultCenters (Optional<Connection> connectionOptional) {
        return QueryLibs.<ResultCenter>executeSelectAll(
            ResultCenter.class,
            QueryTable.ViewResultCenter,
            connectionOptional
        );
    }

    public static Client[] selectAllClients () { return selectAllClients (Optional.empty()); }
    public static Client[] selectAllClients(Optional<Connection> connectionOptional) {
        return QueryLibs.<Client>executeSelectAll(
            Client.class,
            QueryTable.Client,
            connectionOptional
        );
    }

    public static Optional<Client> selectClientById (int id) { return selectClientById (id, Optional.empty()); }
    public static Optional<Client> selectClientById (int id, Optional<Connection> connectionOptional) {
        return executeSelectOne(
            Client.class,
            QueryTable.Client,
            new QueryParam<?>[] {
                new QueryParam<Integer>(TableProperty.Id, id),
            },
            connectionOptional
        );
    }


    public static Appointment[] selectAppointmentByUser (int id) { return selectAppointmentByUser (id, Optional.empty()); }
    public static Appointment[] selectAppointmentByUser (int id, Optional<Connection> connectionOptional) {
        return executeSelect(
            Appointment.class,
            QueryTable.ViewAppointment,
            new QueryParam<?>[] {
                new QueryParam<Integer>(TableProperty.Id, id),
            },
            connectionOptional
        );
    }

    // retorna lista de usuários que são membros de um result center
    public static User[] selectAllUsersInResultCenter (int cr_id) { return selectAllUsersInResultCenter (cr_id, Optional.empty()); }
    public static User[] selectAllUsersInResultCenter (int cr_id, Optional<Connection> connectionOptional) {
        return executeSelect(
            User.class, 
            QueryTable.Member,
            new QueryParam<?>[] {
                new QueryParam<>(TableProperty.ResultCenterId, cr_id),
            },
            connectionOptional
        );
    }

    // retorna lista de centro de resultados dos quais o usuário faz parte
    public static ResultCenter[] selectAllResultCentersOfUser (int user_id) { return selectAllResultCentersOfUser (user_id, Optional.empty()); }
    public static ResultCenter[] selectAllResultCentersOfUser (int user_id, Optional<Connection> connectionOptional) {
        return executeSelect(
            ResultCenter.class, 
            QueryTable.ViewResultCenter,
            new QueryParam<?>[] {
                new QueryParam<>(TableProperty.UserId, user_id),
            },
            connectionOptional
        );
    }

    public static Appointment[] selectAppointmentsByUser (int id) { return selectAppointmentsByUser (id, Optional.empty()); }
    public static Appointment[] selectAppointmentsByUser (int id, Optional<Connection> connectionOptional) {
        return executeSelect(
            Appointment.class, 
            QueryTable.ViewAppointment,
            new QueryParam<?>[] {
                new QueryParam<>(TableProperty.UserId, id),
            },
            connectionOptional
        );
    }

    public static Optional<User> selectUserByEmail (String email) { return selectUserByEmail (email, Optional.empty()); }
    public static Optional<User> selectUserByEmail (String email, Optional<Connection> connectionOptional) {
        return executeSelectOne(
            User.class,
            QueryTable.User,
            new QueryParam<?>[] {
                new QueryParam<String>(TableProperty.Usr_Email, email),
            },
            connectionOptional
        );
    }

/// -----------------------------------------------------------------------------------------------------------------------
/// --------------------------------- FUNÇÕES DE UPDATE -------------------------------------------------------------------
/// -----------------------------------------------------------------------------------------------------------------------

    /// Executa um INSERT na tabela especificada
    private static int executeUpdate (
        QueryTable table,
        QueryParam<?>[] params,
        Optional<Connection> connectionOptional
    ) {

        try {
            ResultSet resultSet = executeQuery(
                new Query(
                    QueryType.UPDATE,
                    table,
                    params
                ),
                connectionOptional
            ).get();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("QueryLibs.executeInsert() -- Erro: nenhum id retornado");
        return -1;
    }

    // Atualiza um apontammento na tabela.
    public static int updateAppointment (Appointment apt) { return updateAppointment (apt, Optional.empty()); }
    public static int updateAppointment (Appointment apt, Optional<Connection> connectionOptional) {
        return executeUpdate(
            QueryTable.Appointment,
            new QueryParam<?>[] {

                // SET
                new QueryParam<Timestamp>(TableProperty.Apt_StartDate, apt.getStart()),
                new QueryParam<Timestamp>(TableProperty.Apt_EndDate, apt.getEnd()),
                new QueryParam<Integer>(TableProperty.UserId, apt.getRequester()),
                new QueryParam<String>(TableProperty.Apt_Project, apt.getProject()),
                new QueryParam<Integer>(TableProperty.Apt_ClientId, apt.getClientId()),
                new QueryParam<Boolean>(TableProperty.Apt_Type, apt.getType().getBooleanValue()),
                new QueryParam<String>(TableProperty.Apt_Justification, apt.getJustification()),
                new QueryParam<Integer>(TableProperty.ResultCenterId, apt.getResultCenterId()),
                new QueryParam<Integer>(TableProperty.Apt_Status, apt.getStatus().getIntValue()),
                new QueryParam<String>(TableProperty.Apt_Feedback, apt.getFeedback()),

                // WHERE
                new QueryParam<Integer>(TableProperty.Id, apt.getId())
            },
            connectionOptional
        );
    }

    public static int updateUser (User user) { return updateUser (user, Optional.empty()); }
    public static int updateUser (User user, Optional<Connection> connectionOptional) {
        return executeUpdate(
            QueryTable.User,
            new QueryParam<?>[] {

                // SET
                new QueryParam<String>(TableProperty.Name, user.getName()),
                new QueryParam<String>(TableProperty.Usr_Registration, user.getRegistration()),
                new QueryParam<Integer>(TableProperty.Usr_Profile, user.getProfile().getProfileLevel()),
                new QueryParam<String>(TableProperty.Usr_Email, user.getEmail()),
    
                // WHERE
                new QueryParam<Integer>(TableProperty.Id, user.getId())
            },
            connectionOptional
        );
    }

    public static void updateUserPassword (int user, String password) { updateUserPassword (user, password, Optional.empty()); }
    public static void updateUserPassword (int user, String password, Optional<Connection> connectionOptional) {
        executeQuery(
            new Query(
                QueryType.UPDATE,
                QueryTable.User,
                new QueryParam<?>[] {
                    // SET
                    new QueryParam<>(TableProperty.Usr_Password, PasswordIncription.encryptPassword(password)),
                    // WHERE
                    new QueryParam<Integer>(TableProperty.Id, user)
                }
            ),
            connectionOptional
        );
    }

    public static int updateClient (Client client) { return updateClient (client, Optional.empty()); }
    public static int updateClient (Client client, Optional<Connection> connectionOptional) {
        return executeUpdate(
            QueryTable.Client,
            new QueryParam<?>[] {

                // SET
                new QueryParam<String>(TableProperty.Clt_RazaoSocial, client.getRazaoSocial()),
                new QueryParam<String>(TableProperty.Clt_CNPJ, client.getCNPJ()),
    
                // WHERE
                new QueryParam<Integer>(TableProperty.Id, client.getId())
            },
            connectionOptional
        );
    }

    public static int updateResultCenter (ResultCenter resultCenter) { return updateResultCenter (resultCenter, Optional.empty()); }
    public static int updateResultCenter (ResultCenter resultCenter, Optional<Connection> connectionOptional) {
        return executeUpdate(
            QueryTable.ResultCenter,
            new QueryParam<?>[] {

                // SET
                new QueryParam<String>(TableProperty.Name, resultCenter.getName()),
                new QueryParam<String>(TableProperty.CR_Sigla, resultCenter.getAcronym()),
                new QueryParam<String>(TableProperty.CR_Codigo, resultCenter.getCode()),
                new QueryParam<Integer>(TableProperty.UserId, resultCenter.getManagerId()),
    
                // WHERE
                new QueryParam<Integer>(TableProperty.Id, resultCenter.getId())
            },
            connectionOptional
        );
    }

/// -----------------------------------------------------------------------------------------------------------------------
/// --------------------------------- FUNÇÕES DE DELETE -------------------------------------------------------------------
/// -----------------------------------------------------------------------------------------------------------------------

    /// Remove um apontamento do banco de dados. TODO: generic
    public static void deleteIdAppointment (Appointment apt) { deleteIdAppointment (apt, Optional.empty()); }
    public static void deleteIdAppointment (Appointment apt, Optional<Connection> connectionOptional) {
        executeQuery(
            new Query(
                QueryType.DELETE,
                QueryTable.Appointment,
                new QueryParam<?>[] {
                    new QueryParam<Integer>(TableProperty.Id, apt.getId())
                }
            ),
            connectionOptional
        );
    }

    public static void deleteUser (User usr_id) { deleteUser (usr_id, Optional.empty()); }
    public static void deleteUser (User usr_id, Optional<Connection> connectionOptional) {
        executeQuery(
            new Query(
                QueryType.DELETE,
                QueryTable.User,
                new QueryParam<?>[] {
                    new QueryParam<Integer>(TableProperty.Id, usr_id.getId())
                }
            ),
            connectionOptional
        );
    }

    public static void deleteClient (int clt_id) { deleteClient (clt_id, Optional.empty()); }
    public static void deleteClient (int clt_id, Optional<Connection> connectionOptional) {
        executeQuery(
            new Query(
                QueryType.DELETE,
                QueryTable.Client,
                new QueryParam<?>[] {
                    new QueryParam<Integer>(TableProperty.Id, clt_id)
                }
            ),
            connectionOptional
        );
    }

    public static void deleteResultCenter (int cr_id) { deleteResultCenter (cr_id, Optional.empty()); }
    public static void deleteResultCenter (int cr_id, Optional<Connection> connectionOptional) {
        executeQuery(
            new Query(
                QueryType.DELETE,
                QueryTable.ResultCenter,
                new QueryParam<?>[] {
                    new QueryParam<Integer>(TableProperty.Id, cr_id)
                }
            ),
            connectionOptional
        );
    }

    public static void removeAllusersFromResultCenter (int cr_id) { removeAllusersFromResultCenter (cr_id, Optional.empty()); }
    public static void removeAllusersFromResultCenter (int cr_id, Optional<Connection> connectionOptional) {
        executeQuery(
            new Query(
                QueryType.DELETE,
                QueryTable.Member,
                new QueryParam<?>[] {
                    new QueryParam<Integer>(TableProperty.ResultCenterId, cr_id)
                }
            ),
            connectionOptional
        );
    }

/// -----------------------------------------------------------------------------------------------------------------------
/// --------------------------------- TESTE DE CONEXÃO  -------------------------------------------------------------------
/// -----------------------------------------------------------------------------------------------------------------------

    public static void testConnection (Connection conexao) {
        // Connection conexao = connect(); // Add param to testConnection so that it doesn't call connect() creating infinite loop

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
