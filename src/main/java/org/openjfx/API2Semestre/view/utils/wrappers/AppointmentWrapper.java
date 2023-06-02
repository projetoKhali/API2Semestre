package org.openjfx.api2semestre.view.utils.wrappers;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.openjfx.api2semestre.appointment.Appointment;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class AppointmentWrapper implements HasSelectedProperty {

    private Appointment appointment;

    public Appointment getAppointment() {
        return appointment;
    }
    private SimpleBooleanProperty selected = new SimpleBooleanProperty(false);

    public AppointmentWrapper (Appointment appointment) {
        this.appointment = appointment;
    }

    public String formatTime (Timestamp timestamp) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date(timestamp.getTime()));
    }

    public String getType() { return appointment.getType().getStringValue(); }
    public String getRequester() { return appointment.getRequesterName(); }
    public String getStartDate() { return formatTime(appointment.getStart()); }
    public String getEndDate() { return formatTime(appointment.getEnd()); }
    public String getResultCenter() { return appointment.getResultCenterName(); }
    public String getClient() { return appointment.getClientName(); }
    public String getProject() { return appointment.getProject(); }
    public String getStatus() { return appointment.getStatus().getStringValue(); }
    public String getTotal() {
        long milliseconds = appointment.getEnd().getTime() - appointment.getStart().getTime();
        long hours = milliseconds / (60 * 60 * 1000);
        long minutes = (milliseconds / (60 * 1000)) % 60;
        if (minutes == 0) return (hours + "h");
        return String.format(hours + ":%02d", minutes);
    }
    public String getJustification() { return appointment.getJustification(); }
    public String getFeedback() { return appointment.getFeedback(); }

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
