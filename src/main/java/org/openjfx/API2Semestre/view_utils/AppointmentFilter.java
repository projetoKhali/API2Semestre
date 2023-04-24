package org.openjfx.api2semestre.view_utils;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.appointments.Appointment;
import org.openjfx.api2semestre.appointments.AppointmentType;
import org.openjfx.api2semestre.appointments.Status;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class AppointmentFilter {

    // public static newDropdownFilter () {
    //     Label aptTypeLabel = new Label("AppointmentType:");
    //     ComboBox<AppointmentType> aptTypeComboBox = new ComboBox<>();
    //     aptTypeComboBox.getItems().addAll(AppointmentType.values());
    //     aptTypeComboBox.getItems().add(0, null); // add an "all" value
    //     aptTypeComboBox.setOnAction(event -> {
    //         AppointmentType selectedAppointmentType = aptTypeComboBox.getValue();
    //         Predicate<Person> filter = (selectedAppointmentType == null) ? apt -> true : apt -> apt.getAppointmentType() == selectedAppointmentType;
    //         tabela.getItems().setAll(aptList.filtered(filter));
    //     });
    //     HBox aptTypeBox = new HBox(aptTypeLabel, aptTypeComboBox);
    //     aptTypeBox.setAlignment(Pos.CENTER_LEFT);
    //     aptTypeBox.setSpacing(5);
    //     aptTypeColumn.setGraphic(aptTypeBox);
    // }
    
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
            if (requester.isPresent() && !appointment.getRequester().contains(requester.get())) return false;
            if (type.isPresent() && !appointment.getType().equals(type.get())) return false;
            if (startDate.isPresent() && !appointment.getStartDate().equals(startDate.get())) return false;
            if (endDate.isPresent() && !appointment.getEndDate().equals(endDate.get())) return false;
            if (squad.isPresent() && !appointment.getSquad().contains(squad.get())) return false;
            if (client.isPresent() && !appointment.getClient().contains(client.get())) return false;
            if (project.isPresent() && !appointment.getProject().contains(project.get())) return false;
            if (justification.isPresent() && !appointment.getJustification().contains(justification.get())) return false;
            if (status.isPresent() && !appointment.getStatus().equals(status.get())) return false;
            return true;
        }).collect(Collectors.toList());

    }
}
