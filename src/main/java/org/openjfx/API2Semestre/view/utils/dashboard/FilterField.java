package org.openjfx.api2semestre.view.utils.dashboard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.data.Client;
import org.openjfx.api2semestre.data.DisplayName;
import org.openjfx.api2semestre.data.HasDisplayName;
import org.openjfx.api2semestre.data.ResultCenter;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.view.controllers.custom_tags.LookupTextField;
import org.openjfx.api2semestre.view.utils.wrappers.AptTypeWrapper;

import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.util.StringConverter;

public enum FilterField implements HasDisplayName {
    AppointmentStart ("Início"),
    AppointmentEnd   ("Fim") ,
    AppointmentType  ("Tipo"),
    ResultCenter     ("Centro de Resultado"),
    Project          ("Projeto"),
    Client           ("Cliente"),
    Requester        ("Colaborador"),
    Manager          ("Gestor");

    private String displayName;

    private FilterField (String displayName) {
        this.displayName = displayName;
    }

    @FunctionalInterface public static interface FilterCallback {
        void execute();
    }

    public Optional<FilterControl> create (
        Appointment[] appointments,
        ReportInterval[] intervals,
        FilterCallback filterCallback,
        Optional<java.sql.Connection> connectionOptional
    ) {
        final BooleanProperty useFilterPredicate = new SimpleBooleanProperty();
        final BooleanProperty datePickerIsEnd = new SimpleBooleanProperty();
        switch (this) {
            case AppointmentEnd: datePickerIsEnd.set(true);
            case AppointmentStart: 
            DatePicker datePicker = new DatePicker();
            datePicker.setPromptText(datePickerIsEnd.get() ? "Data Final" : "Data Início");
            datePicker.valueProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                datePicker,
                datePickerIsEnd.get() ? (Appointment appointment) -> {
                    LocalDate datePickerValue = datePicker.getValue();
                    if (datePickerValue == null) return true;
                    return !appointment.getEnd().toLocalDateTime().isAfter(
                        LocalDateTime.of(datePickerValue, LocalTime.of(0, 0, 0))
                    );
                }
                : (Appointment appointment) -> {
                    LocalDate datePickerValue = datePicker.getValue();
                    if (datePickerValue == null) return true;
                    return !appointment.getStart().toLocalDateTime().isBefore(
                        LocalDateTime.of(datePickerValue, LocalTime.of(0, 0, 0))
                    );
                }
            ));
            
            case AppointmentType: 
            ComboBox<AptTypeWrapper> comboBox = new ComboBox<AptTypeWrapper>();
            List<AptTypeWrapper> comboBoxValues = Arrays.asList(AptTypeWrapper.all());
            comboBox.setItems(FXCollections.observableArrayList(comboBoxValues));
            comboBox.setValue(comboBoxValues.get(0));
            comboBox.setConverter(new StringConverter<AptTypeWrapper>() {
                @Override public String toString(AptTypeWrapper aptTypeWrapper) { return aptTypeWrapper.getName(); }
                @Override public AptTypeWrapper fromString(String string) { return null; }
            });
            comboBox.valueProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                comboBox,
                (Appointment appointment) -> {
                    AptTypeWrapper comboBoxValue = comboBox.getValue();
                    return comboBoxValue == null | appointment.getType().equals(comboBoxValue.getType().orElse(appointment.getType()));
                }
            ));
            
            case ResultCenter: 
            LookupTextField<ResultCenter> ltf_ResultCenter = new LookupTextField<ResultCenter>(
                "Centro de Resultado",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getResultCenterId)
                .distinct()
                .collect(Collectors.toList())
                .stream()
                .map(id -> QueryLibs.selectResultCenter(id, connectionOptional).orElse(null))
                .filter((ResultCenter resultCenter) -> resultCenter != null)
                .toArray(ResultCenter[]::new)
            );
            ltf_ResultCenter.selectedItemProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                ltf_ResultCenter,
                (Appointment appointment) -> {
                    ResultCenter lookupTextFieldSelected = ltf_ResultCenter.getSelectedItem();
                    if (lookupTextFieldSelected == null) return true;
                    return appointment.getResultCenterId() == lookupTextFieldSelected.getId();
                }
            ));

            case Project: 
            LookupTextField<DisplayName> ltf_Project = new LookupTextField<DisplayName>(
                "Projeto",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getProject)
                .distinct()
                .map((String projectName) -> new DisplayName(projectName))
                .collect(Collectors.toList())
                .toArray(DisplayName[]::new)
            );
            ltf_Project.selectedItemProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                ltf_Project,
                (Appointment appointment) -> {
                    DisplayName lookupTextFieldSelected = ltf_Project.getSelectedItem();
                    if (lookupTextFieldSelected == null) return true;
                    return appointment.getProject() == lookupTextFieldSelected.getName();
                }
            ));

            case Client: 
            LookupTextField<Client> ltf_Client = new LookupTextField<Client>(
                "Cliente",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getClientId)
                .distinct()
                .collect(Collectors.toList())
                .stream()
                .map(id -> QueryLibs.selectClientById(id, connectionOptional).orElse(null))
                .filter((Client client) -> client != null)
                .toArray(Client[]::new)
            );
            ltf_Client.selectedItemProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                ltf_Client,
                (Appointment appointment) -> {
                    Client lookupTextFieldSelected = ltf_Client.getSelectedItem();
                    if (lookupTextFieldSelected == null) return true;
                    return appointment.getClientId() == lookupTextFieldSelected.getId();
                }
            ));

            case Manager: useFilterPredicate.set(true);
            case Requester: 
            LookupTextField<User> ltf_User = new LookupTextField<User>(
                useFilterPredicate.get() ? "Gestor" : "Solicitante",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getRequester)
                .distinct()
                .collect(Collectors.toList())
                .stream()
                .map(id -> QueryLibs.selectUserById(id, connectionOptional).orElse(null))
                .filter((User user) -> user != null && (useFilterPredicate.get() ? true : user.getProfile().getProfileLevel() > 0))
                .toArray(User[]::new)
            );
            ltf_User.selectedItemProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                ltf_User,
                (Appointment appointment) -> {
                    User lookupTextFieldSelected = ltf_User.getSelectedItem();
                    if (lookupTextFieldSelected == null) return true;
                    return appointment.getRequester() == lookupTextFieldSelected.getId();
                }
            ));
        }

        System.out.println("Khali | FilterField.create() -- Error: Unsuported FilterField result");
        return Optional.empty();
    }

    @Override public String getName() { return displayName; }

}
