package org.openjfx.api2semestre.view_utils;

import org.openjfx.api2semestre.appointments.VwAppointment;
import org.openjfx.api2semestre.report.ReportInterval;

public class ReportIntervalWrapper {
    
    private VwAppointment appointment;
    private ReportInterval interval;

    public ReportIntervalWrapper (VwAppointment appointment, ReportInterval interval) {
        this.appointment = appointment;
        this.interval = interval;
    }

    public VwAppointment getAppointment() { return appointment; }
    public ReportInterval getInterval() { return interval; }

    public String getMatricula() { return appointment.getMatricula() ; }
    public String getColaborador() { return appointment.getRequester() ; }
    public String getVerba() { return Integer.toString(interval.getVerba()) ; }
    public String getHoraInício() { return interval.getStart().toString() ; }
    public String getHoraFim() { return interval.getEnd().toString() ; }
    public String getTotal() { return interval.getTotal(); }
    public String getCentroResultado() { return appointment.getCrName() ; }
    public String getCliente() { return appointment.getClient() ; }
    public String getProjeto() { return appointment.getProject() ; }
    public String getJustificativa() { return appointment.getJustification() ; }
}
