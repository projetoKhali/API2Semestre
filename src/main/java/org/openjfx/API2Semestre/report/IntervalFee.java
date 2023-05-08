package org.openjfx.api2semestre.report;

import java.io.Externalizable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;

public class IntervalFee {

    // Values
    private int code;
    private double percent;

    // Condition
    private boolean[] daysOfWeek;
    private LocalTime startHour;
    private LocalTime endHour;
    private long minHourCount;
    private boolean cumulative;
    
    public IntervalFee(
        int code,
        Double percent,
        boolean[] daysOfWeek,
        LocalTime startHour,
        LocalTime endHour,
        long minHourCount,
        boolean cumulative
    ) {
        this.code = code;
        this.percent = percent;
        this.daysOfWeek = daysOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
        this.minHourCount = minHourCount;
        this.cumulative = cumulative;
    }
    
    public boolean check (Timestamp appointmentStart, Timestamp appointmentEndTime, int dayHourCount) {
        
        // conversão de Timestamp para LocalTime
        
        LocalDateTime aptStartDateTime = appointmentStart.toLocalDateTime();
        LocalTime aptStartLocalTime = aptStartDateTime.toLocalTime();
        LocalDate aptStartLocalDate = aptStartDateTime.toLocalDate();
        
        LocalDateTime aptEndDateTime = appointmentEndTime.toLocalDateTime();
        LocalTime aptEndLocalTime = aptEndDateTime.toLocalTime();
        LocalDate aptEndLocalDate = aptEndDateTime.toLocalDate();
        
        long numberOfOverlappingMinutes = 0;
        
        // Check if the day of the week falls within the configured range
        LocalDate actualDay = aptStartLocalDate;
        int actualDayOfWeek = actualDay.getDayOfWeek().getValue();
        boolean auxiliar = false; 
        while(!actualDay.isAfter(aptEndLocalDate)){
            // uso %7 pois getDayOfWeek().getValue() considera domingo como sendo 7, e para padronizar quero que seja 0
            if(daysOfWeek[(actualDayOfWeek % 7)]){
                auxiliar = true;
                break;}
                actualDay = actualDay.plusDays(1);
                actualDayOfWeek = actualDay.getDayOfWeek().getValue();
            }
        if(auxiliar == false){return false;}

        // casos em que a verba apenas começa a valer após as 2 horas            
        if (dayHourCount < minHourCount) { 
            System.out.println("\tfails at: dayHourCount(" + dayHourCount + ") < minHourCount(" + minHourCount + ")");
            return false;
        }
            
        // checar se há intersecção com período noturno  
        LocalTime meiaNoite = LocalTime.of(0, 0, 0);
        LocalDateTime verbastart = null;
        LocalDateTime verbaEnd = null;
        LocalDateTime laterStart = null;
        LocalDateTime earlierEnd = null;
        auxiliar = false;
        if((startHour != null) && (endHour != null)){
            actualDay = aptStartLocalDate;
            while(!actualDay.isAfter(aptEndLocalDate)){
                verbastart = actualDay.atTime(startHour);
                if(endHour.isBefore(startHour) & endHour != meiaNoite){
                    verbaEnd = (actualDay.plusDays(1)).atTime(endHour);
                }
                else{
                    verbaEnd = actualDay.atTime(endHour);
                }
                if(verbastart.isAfter(aptEndDateTime) || aptStartDateTime.isAfter(verbaEnd)){
                    numberOfOverlappingMinutes = 0;
                }
                else{
                    laterStart = Collections.max(Arrays.asList(verbastart, aptStartDateTime));
                    earlierEnd = Collections.min(Arrays.asList(verbaEnd, aptEndDateTime));
                    numberOfOverlappingMinutes += ChronoUnit.MINUTES.between(laterStart, earlierEnd);
                    if(numberOfOverlappingMinutes != 0){auxiliar = true;};
                }
                actualDay = actualDay.plusDays(1);
            }
        }
        if(auxiliar == false){return false;}
        
        if(minHourCount != 0 && numberOfOverlappingMinutes != 0){
            numberOfOverlappingMinutes = numberOfOverlappingMinutes - minHourCount;
            return true;
        }

        // The period is within the configured range
        return true;
    }

    // Getters
    public int getCode() { return code; }
    public double getPercent() { return percent; }

    public boolean[] getDaysOfWeek() { return daysOfWeek; }
    public LocalTime getStartHour() { return startHour; }
    public LocalTime getEndHour() { return endHour; }
    public long getMinHourCount() { return minHourCount; }
    public boolean isCumulative() { return cumulative; }

    // Setters
    public void setCode(int code) { this.code = code; }
    public void setPercent(double percent) { this.percent = percent; }    
    
    public void setDaysOfWeek(boolean[] daysOfWeek) { this.daysOfWeek = daysOfWeek; }
    public void setStartHour(LocalTime startHour) { this.startHour = startHour; }
    public void setEndHour(LocalTime endHour) { this.endHour = endHour; }
    public void setMinHourCount(int minHourCount) { this.minHourCount = minHourCount; }    
    public void setCumulative(boolean cumulative) { this.cumulative = cumulative; }

}
