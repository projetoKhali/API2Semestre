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

        final BooleanProperty datePickerIsEnd = new SimpleBooleanProperty(false);
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
            LookupTextField<ResultCenter> ltf_resultcenter = new LookupTextField<ResultCenter>(
                "Centro de Resultado",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getResultCenterId)
                .distinct()
                .map(id -> QueryLibs.selectResultCenter(id, connectionOptional).orElse(null))
                .filter((ResultCenter resultCenter) -> resultCenter != null)
                .toArray(ResultCenter[]::new)
            );
            ltf_resultcenter.selectedItemProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            // ltf_resultcenter.textProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                ltf_resultcenter,
                (Appointment appointment) -> {
                    ResultCenter lookupTextFieldSelected = ltf_resultcenter.getSelectedItem();
                    if (lookupTextFieldSelected == null) return true;
                    return appointment.getResultCenterId() == lookupTextFieldSelected.getId();
                }
            ));

            case Project: 
            LookupTextField<DisplayName> ltf_project = new LookupTextField<DisplayName>(
                "Projeto",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getProject)
                .distinct()
                .map((String projectName) -> new DisplayName(projectName))
                .collect(Collectors.toList())
                .toArray(DisplayName[]::new)
            );
            ltf_project.selectedItemProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                ltf_project,
                (Appointment appointment) -> {
                    DisplayName lookupTextFieldSelected = ltf_project.getSelectedItem();
                    if (lookupTextFieldSelected == null) return true;
                    return appointment.getProject() == lookupTextFieldSelected.getName();
                }
            ));

            case Client: 
            LookupTextField<Client> ltf_client = new LookupTextField<Client>(
                "Cliente",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getClientId)
                .distinct()
                .map(id -> QueryLibs.selectClientById(id, connectionOptional).orElse(null))
                .filter((Client client) -> client != null)
                .toArray(Client[]::new)
            );
            ltf_client.selectedItemProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                ltf_client,
                (Appointment appointment) -> {
                    Client lookupTextFieldSelected = ltf_client.getSelectedItem();
                    if (lookupTextFieldSelected == null) return true;
                    return appointment.getClientId() == lookupTextFieldSelected.getId();
                }
            ));

            case Manager: 
            LookupTextField<User> ltf_manager = new LookupTextField<User>(
                "Gestor",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getResultCenterId)
                .distinct()
                .map(cr_id -> QueryLibs.selectResultCenter(cr_id, connectionOptional).orElse(null))
                .filter((ResultCenter cr) -> cr != null)
                .map(org.openjfx.api2semestre.data.ResultCenter::getManagerId)
                .distinct()
                .map(usr_id -> QueryLibs.selectUserById(usr_id, connectionOptional).orElse(null))
                .filter((User user) -> user != null)
                .toArray(User[]::new)
            );
            System.out.println(ltf_manager.getSuggestions().size() + " user suggestions");
            ltf_manager.selectedItemProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                ltf_manager,
                (Appointment appointment) -> {
                    User lookupTextFieldSelected = ltf_manager.getSelectedItem();
                    if (lookupTextFieldSelected == null) return true;
                    Optional<java.sql.Connection> ltf_manager_connectionoptional = QueryLibs.connect();
                    Optional<ResultCenter> cr = QueryLibs.selectResultCenter(appointment.getResultCenterId(), ltf_manager_connectionoptional);
                    if (cr.isEmpty()) {
                        QueryLibs.close(connectionOptional);
                        return false;
                    }
                    Optional<User> manager = QueryLibs.selectUserById(cr.get().getManagerId(), connectionOptional);
                    QueryLibs.close(connectionOptional);
                    if (manager.isEmpty()) return false;
                    return manager.get().getId() == lookupTextFieldSelected.getId();
                }
            ));


            case Requester: 
            LookupTextField<User> ltf_requester = new LookupTextField<User>(
                "Solicitante",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getRequester)
                .distinct()
                .map(usr_id -> QueryLibs.selectUserById(usr_id, connectionOptional).orElse(null))
                .filter((User user) -> user != null)
                .toArray(User[]::new)
            );
            System.out.println(ltf_requester.getSuggestions().size() + " user suggestions");
            ltf_requester.selectedItemProperty().addListener((observable, oldValue, newValue) -> filterCallback.execute());
            return Optional.ofNullable(new FilterControl(
                ltf_requester,
                (Appointment appointment) -> {
                    User lookupTextFieldSelected = ltf_requester.getSelectedItem();
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
