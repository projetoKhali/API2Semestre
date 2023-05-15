package org.openjfx.api2semestre.database;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.openjfx.api2semestre.appointments.Appointment;
import org.openjfx.api2semestre.appointments.AppointmentType;
import org.openjfx.api2semestre.appointments.VwAppointment;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.authentication.VwUser;
import org.openjfx.api2semestre.data.MemberRelation;
import org.openjfx.api2semestre.data.ResultCenter;

public class Data {

    public static <T extends Data> Data create (Class<T> type, ResultSet resultSet) throws SQLException, Exception {
        if (type == Appointment.class) {
            return new Appointment(
                resultSet.getInt("id"),
                resultSet.getString("requester"),
                AppointmentType.of(resultSet.getBoolean("tipo")),
                new Timestamp(((Date) resultSet.getObject("hora_inicio")).getTime()),
                new Timestamp(((Date) resultSet.getObject("hora_fim")).getTime()),
                resultSet.getString("cr_id"),
                resultSet.getString("cliente"),
                resultSet.getString("projeto"),
                resultSet.getString("justificativa"),
                resultSet.getInt("aprovacao"),
                resultSet.getString("feedback")
            );
        }
        if (type == VwAppointment.class) {
            return new VwAppointment(
                resultSet.getInt("id"),
                resultSet.getString("matricula"),
                resultSet.getString("requester"),
                AppointmentType.of(resultSet.getBoolean("tipo")),
                new Timestamp(((Date) resultSet.getObject("hora_inicio")).getTime()),
                new Timestamp(((Date) resultSet.getObject("hora_fim")).getTime()),
                resultSet.getInt("cr_id"),
                resultSet.getString("centro_nome"),
                resultSet.getString("cliente"),
                resultSet.getString("projeto"),
                resultSet.getString("justificativa"),
                resultSet.getInt("aprovacao"),
                resultSet.getString("feedback")
            );
        }
        if (type == User.class) {
            return new User(
                resultSet.getInt("id"),
                resultSet.getString("nome"),
                Profile.of(resultSet.getInt("Perfil")),
                resultSet.getString("email"),
                resultSet.getString("senha"),
                resultSet.getString("matricula")
            );
        }
        if (type == VwUser.class) {
            return new VwUser(
                resultSet.getInt("id"),
                resultSet.getString("nome"),
                Profile.of(resultSet.getInt("Perfil")),
                resultSet.getString("email"),
                resultSet.getString("senha"),
                resultSet.getString("matricula")
            );
        }
        if (type == ResultCenter.class) {
            return new ResultCenter(
                resultSet.getInt("id"), 
                resultSet.getString("nome"), 
                resultSet.getString("sigla"), 
                resultSet.getString("codigo"),
                resultSet.getInt("usr_id"),
                resultSet.getString("gestor_nome")
            );
        }
        if (type == MemberRelation.class) {
            return new MemberRelation(
                resultSet.getInt("id"), 
                resultSet.getInt("usr_id"), 
                resultSet.getInt("cr_id")
            );
        }
        System.out.println("database.Data.create() -- Tipo de Dado não implementado ou inválido: \"" + type + "\"");
        throw new Exception("| Khali | database.Data.create() -- Erro: Subclasse inválida de \"Data\": \"" + type + "\". Você se certificou de adicionar \"extends Data\" à definição da classe \"" + type + "\" e adicionar um \"if type = ...\" contendo um \"return new ...\" para a classe \"" + type + "\" dentro de \"Data.create()\"?" );
    }
}
