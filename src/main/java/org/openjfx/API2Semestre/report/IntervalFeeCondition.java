package org.openjfx.api2semestre.report;

import java.sql.Timestamp;

public class IntervalFeeCondition {
    private boolean[] daysOfWeek;
    private int startHour;
    private int endHour;
    private int minHourCount;
    
    public IntervalFeeCondition(boolean[] daysOfWeek, int startHour, int endHour, int minHourCount) {
        this.daysOfWeek = daysOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
        this.minHourCount = minHourCount;
    }

    // Returns true if all conditions are met:
    // - Periods overlap;
    // - hourCont matches;
    public boolean check (Timestamp periodStart, Timestamp periodEnd, double hourCount) {

        // Check if the day of the week falls within the configured range
        int dayOfWeek = periodStart.toLocalDateTime().getDayOfWeek().getValue();
        if (!daysOfWeek[dayOfWeek - 1]) return false;
    
        // Check if the start hour is before the configured end hour
        int startHourOfDay = periodStart.toLocalDateTime().getHour();
        if (startHourOfDay >= endHour) return false;
    
        // Check if the end time is after the configured start hour
        int endHourOfDay = periodEnd.toLocalDateTime().getHour();
        if (endHourOfDay <= startHour) return false;

        if (hourCount < minHourCount) return false;
    
        // The period is within the configured range
        return true;
    }

    public boolean[] getDaysOfWeek() { return daysOfWeek; }
    public int getStartHour() { return startHour; }
    public int getEndHour() { return endHour; }
    public int getMinHourCount() { return minHourCount; }

    public void setDaysOfWeek(boolean[] daysOfWeek) { this.daysOfWeek = daysOfWeek; }
    public void setStartHour(int startHour) { this.startHour = startHour; }
    public void setEndHour(int endHour) { this.endHour = endHour; }
    public void setMinHourCount(int minHourCount) { this.minHourCount = minHourCount; }}
