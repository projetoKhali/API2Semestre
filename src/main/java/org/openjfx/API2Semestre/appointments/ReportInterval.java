package org.openjfx.api2semestre.appointments;

import java.sql.Timestamp;

public class ReportInterval {
    private int appointmmentId;
    private Timestamp start;
    private Timestamp end;
    private int verba;

    public ReportInterval(int appointmmentId, Timestamp start, Timestamp end, int verba) {
        this.appointmmentId = appointmmentId;
        this.start = start;
        this.end = end;
        this.verba = verba;
    }
    
    public int getAppointmmentId() { return appointmmentId; }
    public Timestamp getStart() { return start; }
    public Timestamp getEnd() { return end; }
    public int getVerba() { return verba; }
    
    // public void setAppointmmentId (int appointmmentId) { this.appointmmentId = appointmmentId; }
    // public void setStart (Timestamp start) { this.start = start; }
    // public void setEnd (Timestamp end) { this.end = end; }
    // public void setVerba (int verba) { this.verba = verba; }
}
