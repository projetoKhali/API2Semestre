package org.openjfx.api2semestre.data_utils;

import java.security.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openjfx.api2semestre.appointments.Appointment;
import org.openjfx.api2semestre.appointments.AppointmentType;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.report.IntervalFee;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.report.Week;

// os cálculos serão feitos em minutos, e depois divididos por 60 para se chegar a quantidade de horas (em decimal).

public class AppointmentCalculator {
    
    static List<ReportInAppointment> reports = new ArrayList<ReportInAppointment>();
    static List<Appointment> appointments = new ArrayList<Appointment>();
    static Double aptPeriod;
    static LocalDateTime aptStartDateTime; 
    static LocalDateTime aptEndDateTime; 
  
    
    public static List<ReportInterval> calculateReports(){
    // "yyyy-MM-dd HH:mm:ss"
    // public static List<ReportInAppointment> calculateReports(){
        // appointments = Arrays.asList(QueryLibs.collaboratorSelect(Authentication.getCurrentUser().getNome()));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Appointment app1 = new Appointment(
            1, 
            "Julio", 
            AppointmentType.Overtime, 
            DateConverter.stringToTimestamp("2023-05-05 22:00:00"), 
            DateConverter.stringToTimestamp("2023-05-06 15:00:00"), 
            "Squad Foda", 
            "Cleitin", 
            "ProjetoA", 
            "pq sim", 
            0, 
            "sample"
            );

        
        
        // lista paa teste
        appointments.add(app1);
        List<ReportInterval> reportsTemporary = new ArrayList<ReportInterval>();
        List<ReportInterval> reportsFinal = new ArrayList<ReportInterval>();
        for(Appointment apt: appointments){
            aptStartDateTime = apt.getStartDate().toLocalDateTime();
            aptEndDateTime = apt.getEndDate().toLocalDateTime();
            // System.out.println("apt end: " + apt.getEndDate() + " | localdatetime start: " + aptEndDateTime);
            aptPeriod = ((double) ChronoUnit.MINUTES.between(aptStartDateTime, aptEndDateTime))/60;
            // apt1 são as primeiras duas horas do apontamento e apt2 o restante            
            System.out.println("start time do sobreaviso: " + apt.getStartDate() + " | end time do sobreaviso: " + apt.getEndDate());
            Appointment apt1 = apt.copy();
            System.out.println("start time do sobreaviso: " + apt.getStartDate() + " | end time do sobreaviso: " + apt.getEndDate());
            apt1.setEndDate(DateConverter.toTimestamp((apt.getStartDate().toLocalDateTime()).plusHours(2)));
            Appointment apt2 = apt.copy();
            System.out.println("start time do sobreaviso: " + apt.getStartDate() + " | end time do sobreaviso: " + apt.getEndDate());
            apt2.setStartDate(DateConverter.toTimestamp((apt.getStartDate().toLocalDateTime()).plusHours(2)));
            // apt2.setStartDate(DateConverter.toTimestamp((apt2.getStartDate().toLocalDateTime()).plusMinutes(1)));
            
            // retorno de lista com reportIntervals
            if(apt.getType() == AppointmentType.OnNotice){
                System.out.println("é sobreaviso");
                System.out.println("start time do sobreaviso: " + apt.getStartDate() + " | end time do sobreaviso: " + apt.getEndDate());
                reportsTemporary = calculateOnNotice(apt);
                if(reportsTemporary != null){
                    for(ReportInterval repInt: reportsTemporary) reportsFinal.add(repInt);
                }
            }
            else{
                System.out.println("é hora extra");
                for(IntervalFee verba: IntervalFee.VERBAS){
                    if(aptPeriod <= 2){
                        System.out.println("o apontamento tem menor duração que 2 horas");
                        if(verba.getMinHourCount() != 0){continue;}
                        else{
                            reportsTemporary = calculateIntervals(apt, verba);
                            if(reportsTemporary != null){
                                for(ReportInterval repInt: reportsTemporary){
                                    reportsFinal.add(repInt);
                                }
                            }
                        }

                    }
                    else{
                        System.out.println("o apotamento tem maior duração que 2 horas");
                        if(verba.getMinHourCount() != 0){reportsTemporary = calculateIntervals(apt2, verba);
                            System.out.println("verba com horário mínimo");
                            if(reportsTemporary != null){
                                for(ReportInterval repInt: reportsTemporary){
                                    reportsFinal.add(repInt);
                                }
                            }
                        }
                        else{reportsTemporary = calculateIntervals(apt1, verba);
                            if(reportsTemporary != null){
                                for(ReportInterval repInt: reportsTemporary){
                                    reportsFinal.add(repInt);
                                }
                            }
                        }
                        
                    }
                }
            }
        }
        return reportsFinal;
    }    
    
    public static List<ReportInterval> calculateOnNotice(Appointment aptOnNotice){
    // List<ReportInAppointment> calculateOnNotice(Appointment aptOnNotice){
        java.sql.Timestamp onNotice_init = aptOnNotice.getStartDate();
        System.out.println("onNotice_init " + onNotice_init);
        java.sql.Timestamp onNotice_end = aptOnNotice.getEndDate();
        System.out.println("onNotice_end  " + onNotice_end);
        LocalDateTime onotice_init = onNotice_init.toLocalDateTime();
        LocalDateTime onotice_end = onNotice_end.toLocalDateTime();
        java.sql.Timestamp overTime_init = null;
        java.sql.Timestamp overTime_end = null;
        List<ReportInterval> reportsOnNotice = new ArrayList<ReportInterval>();
    
        // calcular a quantidade total de tempo do sobreaviso. Obs: essa função calcula dias, horas e minutos INTEIROS.
        long onNoticeIntervalMinutes = ChronoUnit.MINUTES.between(onotice_init, onotice_end);
        // long onNoticeIntervalSeconds = ChronoUnit.SECONDS.between(onotice_init, onotice_end);
    
    
        // achar intersecção entre sobreaviso e hora extra(acionamento)
        int aux = 0;
        for(Appointment aptOvertime: appointments){
            if (aptOvertime.getType() == AppointmentType.Overtime){
                aux = 1;
                overTime_init = aptOvertime.getStartDate();
                overTime_end = aptOvertime.getEndDate();
                LocalDateTime overtime_init = overTime_init.toLocalDateTime();
                LocalDateTime overtime_end = overTime_end.toLocalDateTime();
    
                if (onotice_end.isBefore(onotice_init) || overtime_end.isBefore(overtime_init)) {
                    System.out.println("Not proper intervals");
                } else {
                    long numberOfOverlappingMinutes;
                    
    
                    if (onotice_end.isBefore(overtime_init) || overtime_end.isBefore(onotice_init)) {
                        // no overlap
                        numberOfOverlappingMinutes = 0;
    
                        
                    } else {
                        LocalDateTime earlierStart = Collections.min(Arrays.asList(onotice_init, overtime_init));
                        LocalDateTime laterStart = Collections.max(Arrays.asList(onotice_init, overtime_init));
                        LocalDateTime earlierEnd = Collections.min(Arrays.asList(onotice_end, overtime_end));
                        LocalDateTime laterEnd = Collections.max(Arrays.asList(onotice_end, overtime_end));
                        numberOfOverlappingMinutes = ChronoUnit.MINUTES.between(laterStart, earlierEnd);
                        if(onotice_init != overtime_init){
                            ReportInterval reportIntervalOnNot = new ReportInterval(
                                aptOvertime.getId(), 
                                DateConverter.toTimestamp(earlierStart), 
                                DateConverter.toTimestamp(laterStart), 
                                3016
                                );
                            reportsOnNotice.add(reportIntervalOnNot);

                        }
                        if(onotice_end != overtime_end){
                            ReportInterval reportIntervalOnNot2 = new ReportInterval(
                                aptOvertime.getId(), 
                                DateConverter.toTimestamp(earlierEnd), 
                                DateConverter.toTimestamp(laterEnd), 
                                3016
                                );
                            reportsOnNotice.add(reportIntervalOnNot2);
                        }
                        
                        
                    }
                   
                    System.out.println("" + numberOfOverlappingMinutes + " minutes of overlap");

                    // do total de tempo de sobreaviso, subtraio a intersecção com hora-extra
                    onNoticeIntervalMinutes = onNoticeIntervalMinutes - numberOfOverlappingMinutes;
                }
            }
        }
        // quando não tem intersecção
        if(aux == 0){ 
            ReportInterval reportIntervalNoIntersection = new ReportInterval(
                aptOnNotice.getId(), 
                onNotice_init, 
                onNotice_end,
                3016
                );
            reportsOnNotice.add(reportIntervalNoIntersection);
            System.out.println("onNotice_init " + onNotice_init );
            System.out.println("onNotice_end " + onNotice_end);
        }
        Double onNoticeIntervalMinutes_Double = (double)onNoticeIntervalMinutes;
        Double onNoticeIntervalHoursDecimal = onNoticeIntervalMinutes_Double/60;
        // return onNoticeIntervalHoursDecimal;
        return reportsOnNotice;

    }

    public static List<ReportInterval> calculateIntervals(Appointment aptOverTime, IntervalFee intervalFee){
            // conversão de Timestamp para LocalTime

        List<ReportInterval> reportsOvertime = new ArrayList<ReportInterval>();
            
        LocalDateTime aptStartDateTime = aptOverTime.getStartDate().toLocalDateTime();
        LocalTime aptStartLocalTime = aptStartDateTime.toLocalTime();
        LocalDate aptStartLocalDate = aptStartDateTime.toLocalDate();
        
        LocalDateTime aptEndDateTime = aptOverTime.getEndDate().toLocalDateTime();
        LocalTime aptEndLocalTime = aptEndDateTime.toLocalTime();
        LocalDate aptEndLocalDate = aptEndDateTime.toLocalDate();
        
        long numberOfOverlappingMinutes = 0;
        long dayHourCount = ChronoUnit.MINUTES.between(aptStartDateTime, aptEndDateTime);

                                
        // Check if the day of the week falls within the configured range
        LocalDate actualDay = aptStartLocalDate;
        int actualDayOfWeek = actualDay.getDayOfWeek().getValue();
        boolean auxiliar = false; 
        while(!actualDay.isAfter(aptEndLocalDate)){
            // uso %7 pois getDayOfWeek().getValue() considera domingo como sendo 7, e para padronizar quero que seja 0
            if(intervalFee.getDaysOfWeek()[(actualDayOfWeek % 7)]){
                auxiliar = true;
                break;}
                actualDay = actualDay.plusDays(1);
                actualDayOfWeek = actualDay.getDayOfWeek().getValue();
            }
        if(auxiliar == false){return null;}

        // casos em que a verba apenas começa a valer após as 2 horas            
        if (dayHourCount < intervalFee.getMinHourCount()) { 
            System.out.println("\tfails at: dayHourCount(" + dayHourCount + ") < minHourCount(" + intervalFee.getMinHourCount() + ")");
            return null;
        }
            
        // checar se há intersecção com período noturno  
        LocalTime meiaNoite = LocalTime.of(0, 0, 0);
        // LocalTime onzeE59 = LocalTime.of(23, 59, 59);
        LocalDateTime verbastart = null;
        LocalDateTime verbaEnd = null;
        LocalDateTime laterStart = null;
        LocalDateTime earlierEnd = null;
        auxiliar = false;
        if((intervalFee.getStartHour() != null) && (intervalFee.getEndHour() != null)){
            actualDay = aptStartLocalDate;
            while(!actualDay.isAfter(aptEndLocalDate)){
                actualDayOfWeek = actualDay.getDayOfWeek().getValue();

                // cálculo da LocalDateTie de início e fim da verba
                if(intervalFee.getDaysOfWeek()[(actualDayOfWeek % 7)]){
                    verbastart = actualDay.atTime(intervalFee.getStartHour());
                    // situação em a hora termina no dia seguinte
                    // if(intervalFee.getEndHour().isBefore(intervalFee.getStartHour()) & intervalFee.getEndHour() != meiaNoite){
                    if(intervalFee.getEndHour().isBefore(intervalFee.getStartHour())){
                        // se no dia seguinte a verba é válida
                        if(intervalFee.getDaysOfWeek()[((actualDayOfWeek+1) % 7)]){
                            verbaEnd = (actualDay.plusDays(1)).atTime(intervalFee.getEndHour());
                        }
                        // se no dia seguinte a verba não está ativa
                        else{
                            verbaEnd = actualDay.plusDays(1).atTime(0, 0);
                        }
                    }
                    // situação em que a hora começa e termina no mesmo dia
                    else{
                        verbaEnd = actualDay.atTime(intervalFee.getEndHour());
                    }
                }
                else if(intervalFee.getDaysOfWeek()[((actualDayOfWeek+1) % 7)]){
                    verbastart = (actualDay.plusDays(1)).atTime(meiaNoite);
                    verbaEnd = (actualDay.plusDays(1)).atTime(intervalFee.getEndHour());
                }

                // cálculo da intersecção
                if(verbastart.isAfter(aptEndDateTime) || aptStartDateTime.isAfter(verbaEnd)){
                    numberOfOverlappingMinutes = 0;
                    
                }
                else{
                    laterStart = Collections.max(Arrays.asList(verbastart, aptStartDateTime));
                    earlierEnd = Collections.min(Arrays.asList(verbaEnd, aptEndDateTime));
                    numberOfOverlappingMinutes += ChronoUnit.MINUTES.between(laterStart, earlierEnd);
                    if(numberOfOverlappingMinutes != 0){auxiliar = true;};
                    ReportInterval reportInterval = new ReportInterval(
                        aptOverTime.getId(), 
                        DateConverter.toTimestamp(laterStart), 
                        DateConverter.toTimestamp(aptEndDateTime),
                        intervalFee.getCode());
                        reportsOvertime.add(reportInterval);
                }
                
                actualDay = actualDay.plusDays(1);
            }
        }
        // if(auxiliar == false){return 0.0;}
        
        if(intervalFee.getMinHourCount() != 0 && numberOfOverlappingMinutes != 0){
            numberOfOverlappingMinutes = numberOfOverlappingMinutes - intervalFee.getMinHourCount();
        }
        
        Double overlappingMinutes = (double)numberOfOverlappingMinutes;
                
        return reportsOvertime;
    }

}
       
