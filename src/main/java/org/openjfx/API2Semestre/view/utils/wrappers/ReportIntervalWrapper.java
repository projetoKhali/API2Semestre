package org.openjfx.api2semestre.view.utils.wrappers;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.report.ReportInterval;

public class ReportIntervalWrapper {

    private Appointment appointment;
    private ReportInterval interval;

    public ReportIntervalWrapper (Appointment appointment, ReportInterval interval) {
        this.appointment = appointment;
        this.interval = interval;
    }

    public Appointment getAppointment() { return appointment; }
    public ReportInterval getInterval() { return interval; }

    public String getRequesterRegistration() { return appointment.getRequesterRegistration(); }
    public String getRequesterName() { return appointment.getRequesterName(); }
    public String getIntervalFeeCode() { return Integer.toString(interval.getVerba()); }
    public String getStart() {
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date(interval.getStart().getTime()));
    }
    public String getEnd() {
        return new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new java.util.Date(interval.getEnd().getTime()));
    }
    public String getTotal() { return interval.getTotalString(); }
    public String getResultCenterName() { return appointment.getResultCenterName(); }
    public String getClientName() { return appointment.getClientName(); }
    public String getProjectName() { return appointment.getProject(); }
    public String getJustification() { return appointment.getJustification(); }
}
