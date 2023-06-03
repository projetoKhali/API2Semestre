package org.openjfx.api2semestre.view.utils.dashboard;

import java.util.Arrays;
import java.util.List;
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
import javafx.scene.control.Control;
import javafx.scene.control.Label;
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

    public Control create(Appointment[] appointments, ReportInterval[] intervals) {
        final BooleanProperty useFilterPredicate = new SimpleBooleanProperty();
        switch (this) {
            case AppointmentStart: case AppointmentEnd: 
            return new Label("test");
            
            case AppointmentType: 
            ComboBox<AptTypeWrapper> comboBox = new ComboBox<AptTypeWrapper>();
            List<AptTypeWrapper> comboBoxValues = Arrays.asList(AptTypeWrapper.all());
            comboBox.setItems(FXCollections.observableArrayList(comboBoxValues));
            comboBox.setValue(comboBoxValues.get(0));
            comboBox.setConverter(new StringConverter<AptTypeWrapper>() {
                @Override public String toString(AptTypeWrapper aptTypeWrapper) { return aptTypeWrapper.getName(); }
                @Override public AptTypeWrapper fromString(String string) { return null; }
            });
            return comboBox;
            
            case ResultCenter: 
            return new LookupTextField<ResultCenter>(
                "Centro de Resultado",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getResultCenterId)
                .distinct()
                .collect(Collectors.toList())
                .stream()
                .map(id -> QueryLibs.selectResultCenter(id).orElseThrow())
                .toArray(ResultCenter[]::new)
            );

            case Project: 
            return new LookupTextField<DisplayName>(
                "Projeto",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getProject)
                .distinct()
                .map((String projectName) -> new DisplayName(projectName))
                .collect(Collectors.toList())
                .toArray(DisplayName[]::new)
            );

            case Client: 
            return new LookupTextField<Client>(
                "Cliente",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getClientId)
                .distinct()
                .collect(Collectors.toList())
                .stream()
                .map(id -> QueryLibs.selectClientById(id).orElseThrow())
                .toArray(Client[]::new)
            );

            case Manager: useFilterPredicate.set(true);
            case Requester: 
            return new LookupTextField<User>(
                useFilterPredicate.get() ? "Gestor" : "Solicitante",
                Arrays.asList(appointments)
                .stream()
                .map(Appointment::getRequester)
                .distinct()
                .collect(Collectors.toList())
                .stream()
                .map(id -> QueryLibs.selectUserById(id).orElseThrow())
                .filter((User user) -> useFilterPredicate.get() ? true : user.getProfile().getProfileLevel() > 0)
                .toArray(User[]::new)
            );
        }
        return null;
    }

    @Override public String getName() { return displayName; }

}
