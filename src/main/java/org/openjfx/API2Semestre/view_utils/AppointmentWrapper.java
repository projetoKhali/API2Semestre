package org.openjfx.api2semestre.view_utils;

import java.sql.Timestamp;

import org.openjfx.api2semestre.classes.Appointment;

public class AppointmentWrapper {

    private Appointment appointment;
    private Boolean selected;

    public AppointmentWrapper (Appointment appointment) {
        this.appointment = appointment;
    }

    public String getType() { return appointment.getType().getStringValue(); }
    public String getRequester() { return appointment.getRequester(); }
    public Timestamp getStartDate() { return appointment.getStartDate(); }
    public Timestamp getEndDate() { return appointment.getEndDate(); }
    public String getSquad() { return appointment.getSquad(); }
    public String getClient() { return appointment.getClient(); }
    public String getProject() { return appointment.getProject(); }
    public String getJustification() { return appointment.getJustification(); }
    public String getStatus() { return appointment.getStatus().getStringValue(); }
    public String getTotal() {
        long milliseconds = getEndDate().getTime() - getStartDate().getTime();
        long hours = milliseconds / (60 * 60 * 1000);
        long minutes = (milliseconds / (60 * 1000)) % 60;
        if (minutes == 0) return (hours + "h");
        return String.format("%:%02d", hours, minutes);
    }

    public Boolean isSelected() {
        return selected;
    }

    public AppointmentWrapper setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

}
