package org.openjfx.api2semestre.report;

import java.sql.Timestamp;

public class IntervalFee {

    // Values
    private int code;
    private double percent;

    // Condition
    private boolean[] daysOfWeek;
    private int startHour;
    private int endHour;
    private int minHourCount;
    private boolean cumulative;

    public IntervalFee(
        int code,
        float percent,
        boolean[] daysOfWeek,
        int startHour,
        int endHour,
        int minHourCount,
        boolean cumulative
    ) {
        this.code = code;
        this.percent = percent;
        this.daysOfWeek = daysOfWeek;
        this.startHour = ((startHour % 24) + 24) % 24;
        this.endHour = ((endHour % 24) + 24) % 24;
        this.minHourCount = minHourCount;
        this.cumulative = cumulative;
    }

    public boolean check (Timestamp appointmentStartTime, Timestamp appointmentEndTimem, double dayHourCount) {

        // Check if the day of the week falls within the configured range
        int appointmentDayOfWeek = appointmentStartTime.toLocalDateTime().getDayOfWeek().getValue();
        if (!daysOfWeek[appointmentDayOfWeek - 1]) { 
            System.out.println("\tfails at: !daysOfWeek[appointmentDayOfWeek - 1]");
            return false;
        }

        if (startHour != 0 || endHour != 0) {
        
            // Check if the start hour is before the configured end hour
            int appointmentStartHour = appointmentStartTime.toLocalDateTime().getHour();
            if (appointmentStartHour >= endHour) { 
                System.out.println("\tfails at: appointmentStartHour(" + appointmentStartHour + ") >= endHour(" + endHour + ")");
                return false;
            }
        
            // Check if the end time is after the configured start hour
            int appointmentEndHour = appointmentEndTimem.toLocalDateTime().getHour();
            if (appointmentEndHour <= startHour) { 
                System.out.println("\tfails at: appointmentEndHour(" + appointmentEndHour + ") <= startHour(" + startHour + ")");
                return false;
            }
        }

        if (dayHourCount < minHourCount) { 
            System.out.println("\tfails at: dayHourCount(" + dayHourCount + ") < minHourCount(" + minHourCount + ")");
            return false;
        }
    
        // The period is within the configured range
        return true;
    }

    // Getters
    public int getCode() { return code; }
    public double getPercent() { return percent; }

    public boolean[] getDaysOfWeek() { return daysOfWeek; }
    public int getStartHour() { return startHour; }
    public int getEndHour() { return endHour; }
    public int getMinHourCount() { return minHourCount; }
    public boolean isCumulative() { return cumulative; }

    // Setters
    public void setCode(int code) { this.code = code; }
    public void setPercent(double percent) { this.percent = percent; }    
    
    public void setDaysOfWeek(boolean[] daysOfWeek) { this.daysOfWeek = daysOfWeek; }
    public void setStartHour(int startHour) { this.startHour = startHour; }
    public void setEndHour(int endHour) { this.endHour = endHour; }
    public void setMinHourCount(int minHourCount) { this.minHourCount = minHourCount; }    
    public void setCumulative(boolean cumulative) { this.cumulative = cumulative; }

}
