package org.openjfx.api2semestre.view_utils;

import java.sql.Timestamp;

import org.openjfx.api2semestre.classes.Appointment;
import org.openjfx.api2semestre.classes.Status;

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
    public String getStatus() {
        Status status = appointment.getStatus();
        // if (status == null) { return Status.STATUS[0].getStringValue(); } // status should never be null
        return status.getStringValue();
    }

    public Boolean isSelected() {
        return selected;
    }

    public AppointmentWrapper setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }

}
