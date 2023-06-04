package org.openjfx.api2semestre.view.utils.filters;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.appointment.AppointmentType;
import org.openjfx.api2semestre.appointment.Status;
import org.openjfx.api2semestre.view.utils.wrappers.AppointmentWrapper;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class AppointmentFilter {

    public static List<Appointment> filterFromView (
        List<Appointment> appointments,
        Optional<TableColumn<AppointmentWrapper, String>> col_requester,
        Optional<TableColumn<AppointmentWrapper, String>> col_tipo,
        Optional<TableColumn<AppointmentWrapper, String>> col_inicio,
        Optional<TableColumn<AppointmentWrapper, String>> col_fim,
        Optional<TableColumn<AppointmentWrapper, String>> col_squad,
        Optional<TableColumn<AppointmentWrapper, String>> col_cliente,
        Optional<TableColumn<AppointmentWrapper, String>> col_projeto,
        Optional<Status> status
    ) {
        return apply(
            appointments,
            getValueOfTextField(col_requester),
            Optional.empty(), // filter based on dropdown instead of string
            Optional.empty(),
            Optional.empty(),
            getValueOfTextField(col_squad),
            getValueOfTextField(col_cliente),
            getValueOfTextField(col_projeto),
            Optional.empty(),
            status
        );
    }

    private static Optional<String> getValueOfTextField (Optional<TableColumn<AppointmentWrapper, String>> column) {
        if (!column.isPresent()) return Optional.empty();
        var header = column.get().getGraphic();
        if (header instanceof TextField) return Optional.of(((TextField)header).getText());
        if (header instanceof Label) return Optional.of(((Label)header).getText());
        return Optional.empty();
    }

    public static List<Appointment> apply (
        List<Appointment> appointments,
        Optional<String> requester,
        Optional<AppointmentType> type,
        Optional<Timestamp> startDate,
        Optional<Timestamp> endDate,
        Optional<String> squad,
        Optional<String> client,
        Optional<String> project,
        Optional<String> justification,
        Optional<Status> status
    ) {
        return appointments.stream().filter((Appointment appointment) -> {
            if (requester.isPresent() && !appointment.getRequester().toLowerCase().contains(requester.get().toLowerCase())) return false;
            if (type.isPresent() && !appointment.getType().equals(type.get())) return false;
            if (startDate.isPresent() && !appointment.getStart().equals(startDate.get())) return false;
            if (endDate.isPresent() && !appointment.getEnd().equals(endDate.get())) return false;
            if (squad.isPresent() && !appointment.getSquad().toLowerCase().contains(squad.get().toLowerCase())) return false;
            if (client.isPresent() && !appointment.getClient().toLowerCase().contains(client.get().toLowerCase())) return false;
            if (project.isPresent() && !appointment.getProject().toLowerCase().contains(project.get().toLowerCase())) return false;
            if (justification.isPresent() && !appointment.getJustification().toLowerCase().contains(justification.get().toLowerCase())) return false;
            if (status.isPresent() && !appointment.getStatus().equals(status.get())) return false;
            return true;
        }).collect(Collectors.toList());

    }
}
