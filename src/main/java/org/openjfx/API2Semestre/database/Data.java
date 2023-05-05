package org.openjfx.api2semestre.database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.openjfx.api2semestre.appointments.Appointment;
import org.openjfx.api2semestre.appointments.AppointmentType;
// import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;

public class Data {

    public static <T extends Data> Data create (Class<T> type, ResultSet resultSet) throws SQLException, Exception {
        if (type == Appointment.class) {
            return new Appointment(
                resultSet.getInt("apt_id"),
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
        if (type == User.class) {
            // return new User(
            //     // resultSet.getInt("usr_id"),
            //     resultSet.getString("nome"),
            //     Profile.of(resultSet.getInt("tipo"))
            // );
        }
        System.out.println("database.Data.create() -- Tipo de Dado não implementado ou inválido: \"" + type + "\"");
        throw new Exception("Khali | database.Data.create() -- Erro: Subclasse inválida de \"Data\": \"" + type + "\"" );
    }
}
