package org.openjfx.api2semestre.database;

import java.util.Date;
import java.util.Optional;
import java.sql.ResultSet;
import java.sql.Timestamp;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.appointment.AppointmentType;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.data.Client;
import org.openjfx.api2semestre.data.MemberRelation;
import org.openjfx.api2semestre.data.ResultCenter;

public class Data {

    public static <T extends Data> Optional<Data> create (Class<T> type, ResultSet resultSet) {
        try {
            if (type == Appointment.class) return Optional.of(new Appointment(
                resultSet.getInt("id"),                                                 // Integer id
                resultSet.getInt("usr_id"),                                             // int requester
                resultSet.getString("matricula"),                                       // String requester_registration
                resultSet.getString("requester"),                                       // String requester_name
                AppointmentType.of(resultSet.getBoolean("tipo")),                       // AppointmentType type
                new Timestamp(((Date) resultSet.getObject("hora_inicio")).getTime()),   // Timestamp startDate
                new Timestamp(((Date) resultSet.getObject("hora_fim")).getTime()),      // Timestamp endDate
                resultSet.getInt("cr_id"),                                           // int resultCenterId
                resultSet.getString("centro_nome"),                                        // int resultCenterName
                resultSet.getInt("clt_id"),                                          // int clientId
                resultSet.getString("cliente_nome"),                                       // int clientName
                resultSet.getString("projeto"),                                         // String project
                resultSet.getString("justificativa"),                                   // String justification
                resultSet.getInt("aprovacao"),                                          // Status status
                resultSet.getString("feedback")                                         // String feedback
            ));
            if (type == User.class) return Optional.of(new User(
                resultSet.getInt("id"),
                resultSet.getString("nome"),
                Profile.of(resultSet.getInt("Perfil")),
                resultSet.getString("email"),
                resultSet.getString("senha"),
                resultSet.getString("matricula")
            ));
            if (type == ResultCenter.class) return Optional.of(new ResultCenter(
                resultSet.getInt("id"), 
                resultSet.getString("nome"), 
                resultSet.getString("sigla"), 
                resultSet.getString("codigo"),
                resultSet.getInt("usr_id"),
                resultSet.getString("gestor_nome")
            ));
            if (type == MemberRelation.class) return Optional.of(new MemberRelation(
                resultSet.getInt("id"), 
                resultSet.getInt("usr_id"), 
                resultSet.getInt("cr_id")
            ));
            if (type == Client.class) return Optional.of(new Client(
                resultSet.getInt("id"), 
                resultSet.getString("razao_social"),
                resultSet.getString("cnpj")
            ));
            System.out.println("database.Data.create() -- Tipo de Dado não implementado ou inválido: \"" + type + "\"");
            throw new Exception("| Khali | database.Data.create() -- Erro: Subclasse inválida de \"Data\": \"" + type + "\". Você se certificou de adicionar \"extends Data\" à definição da classe \"" + type + "\" e adicionar um \"if type = ...\" contendo um \"return new ...\" para a classe \"" + type + "\" dentro de \"Data.create()\"?" );
        } catch (Exception e) {
            System.out.println("Data.create() -- Erro ao instanciar dado resultante");
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
