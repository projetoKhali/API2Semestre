package org.openjfx.api2semestre.view_utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.openjfx.api2semestre.classes.Appointment;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class AppointmentWrapper implements HasSelectedProperty {

    private Appointment appointment;
    private SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public AppointmentWrapper (Appointment appointment) {
        this.appointment = appointment;
    }

    public String formatTime (Timestamp timestamp) {
        return new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date(timestamp.getTime()));
    }

    public String getType() { return appointment.getType().getStringValue(); }
    public String getRequester() { return appointment.getRequester(); }
    public String getStartDate() { return formatTime(appointment.getStartDate()); }
    public String getEndDate() { return formatTime(appointment.getEndDate()); }
    public String getSquad() { return appointment.getSquad(); }
    public String getClient() { return appointment.getClient(); }
    public String getProject() { return appointment.getProject(); }
    public String getJustification() { return appointment.getJustification(); }
    public String getStatus() { return appointment.getStatus().getStringValue(); }
    public String getTotal() {
        long milliseconds = appointment.getEndDate().getTime() - appointment.getStartDate().getTime();
        long hours = milliseconds / (60 * 60 * 1000);
        long minutes = (milliseconds / (60 * 1000)) % 60;
        if (minutes == 0) return (hours + "h");
        return String.format("%:%02d", hours, minutes);
    }

    @Override
    public BooleanProperty selectedProperty() {
        return selected;
    }

    @Override
    public boolean getSelected() {
        return selected.get();
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

}
