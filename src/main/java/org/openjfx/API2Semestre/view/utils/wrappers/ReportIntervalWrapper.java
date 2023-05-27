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

    public String getRequesterRegistration() { return appointment.getRequesterRegistration() ; }
    public String getColaborador() { return appointment.getRequesterName() ; }
    public String getVerba() { return Integer.toString(interval.getVerba()) ; }
    public String getHoraInício() { return interval.getStart().toString() ; }
    public String getHoraFim() { return interval.getEnd().toString() ; }
    public String getTotal() { return interval.getTotal(); }
    public String getCentroResultado() { return appointment.getResultCenterName() ; }
    public String getCliente() { return appointment.getClientName() ; }
    public String getProjeto() { return appointment.getProject() ; }
    public String getJustificativa() { return appointment.getJustification() ; }
}
