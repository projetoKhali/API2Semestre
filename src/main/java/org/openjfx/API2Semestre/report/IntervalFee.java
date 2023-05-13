package org.openjfx.api2semestre.report;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;

import org.openjfx.api2semestre.appointments.AppointmentType;

public class IntervalFee {
    static final AppointmentType Overtime = AppointmentType.Overtime;

    // Values
    private int code;
    private double hourDuration;
    private double percent;

    // Condition
    private AppointmentType type;
    private boolean[] daysOfWeek;
    private LocalTime startHour;
    private LocalTime endHour;
    private long minHourCount;
    private boolean cumulative;
    
    public IntervalFee(
        int code,
        double hourDuration,
        double percent,
        AppointmentType type,
        boolean[] daysOfWeek,
        LocalTime startHour,
        LocalTime endHour,
        long minHourCount,
        boolean cumulative
    ) {
        this.code = code;
        this.hourDuration = hourDuration;
        this.percent = percent;
        this.type = type;
        this.daysOfWeek = daysOfWeek;
        this.startHour = startHour;
        this.endHour = endHour;
        this.minHourCount = minHourCount;
        this.cumulative = cumulative;
    }
    // cadastro de vergas de hora-extra

    // new IntervalFee(1001, 1.25f, Week.FDS.get(), 0, 0, 0, false),
    public static final IntervalFee[] VERBAS = new IntervalFee[] {
        new IntervalFee(1602, 1.0,    1.00, Overtime, Week.FDS.get(), LocalTime.of(6, 0, 0), LocalTime.of(18, 0, 0), 0, false),
        new IntervalFee(1602, 1.0,    1.00, Overtime, Week.UTEIS.get(), LocalTime.of(6, 0, 0), LocalTime.of(18, 0, 0), 2, false),
        new IntervalFee(1601, 1.0,    0.75, Overtime, Week.UTEIS.get(), LocalTime.of(6, 0, 0), LocalTime.of(18, 0, 0), 0, false),
        new IntervalFee(3001, 1.1429, 1.00, Overtime, Week.FDS.get(), LocalTime.of(18, 1, 0), LocalTime.of(05, 59, 0), 0, false),
        new IntervalFee(3001, 1.1429, 1.00, Overtime, Week.UTEIS.get(), LocalTime.of(18, 1, 0), LocalTime.of(05, 59, 0), 2, false),
        new IntervalFee(3000, 1.1429, 0.75, Overtime, Week.UTEIS.get(), LocalTime.of(18, 1, 0), LocalTime.of(05, 59, 0),  0, false),
        new IntervalFee(1809, 1.1429, 0.3, Overtime, Week.ALL.get(), LocalTime.of(18, 1, 0), LocalTime.of(05, 59, 0),  0, true),
    };

    
    
    // conversão de Timestamp para LocalTime
    public Double check (Timestamp appointmentStart, Timestamp appointmentEndTime, long dayHourCount) {
        
        
        LocalDateTime aptStartDateTime = appointmentStart.toLocalDateTime();
        LocalDate aptStartLocalDate = aptStartDateTime.toLocalDate();
        
        LocalDateTime aptEndDateTime = appointmentEndTime.toLocalDateTime();
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
        if(auxiliar == false){return 0.0;}

        // casos em que a verba apenas começa a valer após as 2 horas            
        if (dayHourCount < minHourCount) { 
            System.out.println("\tfails at: dayHourCount(" + dayHourCount + ") < minHourCount(" + minHourCount + ")");
            return 0.0;
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
                actualDayOfWeek = actualDay.getDayOfWeek().getValue();
                if(daysOfWeek[(actualDayOfWeek % 7)]){
                    verbastart = actualDay.atTime(startHour);
                    if(endHour.isBefore(startHour) & endHour != meiaNoite){
                        if(daysOfWeek[((actualDayOfWeek+1) % 7)]){
                            verbaEnd = (actualDay.plusDays(1)).atTime(endHour);
                        }
                        else{
                            verbaEnd = meiaNoite.atDate(actualDay);
                        }
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
                }
                actualDay = actualDay.plusDays(1);
            }
        }
        if(auxiliar == false){return 0.0;}
        
        if(minHourCount != 0 && numberOfOverlappingMinutes != 0){
            numberOfOverlappingMinutes = numberOfOverlappingMinutes - minHourCount;
        }
        
        Double overlappingMinutes = (double)numberOfOverlappingMinutes;
        return overlappingMinutes/60;
        // return true;
    }

    // Getters
    public int getCode() { return code; }
    public double getPercent() { return percent; }
    public double getHourDuration() { return hourDuration; }

    public AppointmentType getType() { return type; }
    public boolean[] getDaysOfWeek() { return daysOfWeek; }
    public LocalTime getStartHour() { return startHour; }
    public LocalTime getEndHour() { return endHour; }
    public long getMinHourCount() { return minHourCount; }
    public boolean isCumulative() { return cumulative; }

    // Setters
    public void setCode(int code) { this.code = code; }
    public void setPercent(double percent) { this.percent = percent; }    
    public void setHourDuration(double hourDuration) { this.hourDuration = hourDuration; }

    public void setType(AppointmentType type) { this.type = type; }
    public void setDaysOfWeek(boolean[] daysOfWeek) { this.daysOfWeek = daysOfWeek; }
    public void setStartHour(LocalTime startHour) { this.startHour = startHour; }
    public void setEndHour(LocalTime endHour) { this.endHour = endHour; }
    public void setMinHourCount(int minHourCount) { this.minHourCount = minHourCount; }    
    public void setCumulative(boolean cumulative) { this.cumulative = cumulative; }

}
