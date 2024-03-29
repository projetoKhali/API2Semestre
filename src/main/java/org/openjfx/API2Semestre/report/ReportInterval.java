package org.openjfx.api2semestre.report;

import java.sql.Timestamp;
import java.time.Duration;

public class ReportInterval {
    private int appointmmentId;
    private int verba;
    private Timestamp start;
    private Timestamp end;

    public ReportInterval(int appointmmentId, int verba, Timestamp start, Timestamp end) {
        this.appointmmentId = appointmmentId;
        this.verba = verba;
        this.start = start;
        this.end = end;
    }
    
    public long getTotal() {
        return Duration.between(getStart().toInstant(), getEnd().toInstant()).getSeconds() / 60;
    }

    public int getAppointmmentId() { return appointmmentId; }
    public int getVerba() { return verba; }
    public Timestamp getStart() { return start; }
    public Timestamp getEnd() { return end; }
    public String getTotalString() {
        long minutes = getTotal();
        return new StringBuilder(String.valueOf(minutes / 60)).append(":").append(minutes % 60).toString();
    }

    public void setAppointmmentId (int appointmmentId) { this.appointmmentId = appointmmentId; }
    public void setStart (Timestamp start) { this.start = start; }
    public void setEnd (Timestamp end) { this.end = end; }
    public void setVerba (int verba) { this.verba = verba; }
}
