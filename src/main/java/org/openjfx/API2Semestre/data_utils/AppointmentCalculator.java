package org.openjfx.api2semestre.data_utils;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openjfx.api2semestre.appointments.Appointment;
import org.openjfx.api2semestre.appointments.AppointmentType;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.database.QueryLibs;

// os cálculos serão feitos em minutos, e depois divididos por 60 para se chegar a quantidade de horas (em decimal).

public class AppointmentCalculator {
    
    static List<ReportInAppointment> reports = new ArrayList<ReportInAppointment>();
    static List<Appointment> appointments = new ArrayList<Appointment>();
  
    
    public static void calculateReports(){
    // public static List<ReportInAppointment> calculateReports(){
        appointments = Arrays.asList(QueryLibs.collaboratorSelect(Authentication.getCurrentUser().getNome()));
        for(Appointment apt: appointments){
            if(apt.getType() == AppointmentType.OnNotice){
                calculateOnNotice(apt);
            }
        }
    }    
    
    public static Double calculateOnNotice(Appointment aptOnNotice){
    // List<ReportInAppointment> calculateOnNotice(Appointment aptOnNotice){
        java.sql.Timestamp onNotice_init = aptOnNotice.getStartDate();
        java.sql.Timestamp onNotice_end = aptOnNotice.getEndDate();
        LocalDateTime onotice_init = onNotice_init.toLocalDateTime();
        LocalDateTime onotice_end = onNotice_end.toLocalDateTime();
        java.sql.Timestamp overTime_init = null;
        java.sql.Timestamp overTime_end = null;
    
        // calcular a quantidade total de tempo do sobreaviso. Obs: essa função calcula dias, horas e minutos INTEIROS.
        long onNoticeIntervalMinutes = ChronoUnit.MINUTES.between(onotice_init, onotice_end);
        // long onNoticeIntervalSeconds = ChronoUnit.SECONDS.between(onotice_init, onotice_end);
    
    
        // achar intersecção entre sobreaviso e hora extra(acionamento)
        for(Appointment aptOvertime: appointments){
            if (aptOvertime.getType() == AppointmentType.Overtime){
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
                        LocalDateTime laterStart = Collections.max(Arrays.asList(onotice_init, overtime_init));
                        LocalDateTime earlierEnd = Collections.min(Arrays.asList(onotice_end, overtime_end));
                        numberOfOverlappingMinutes = ChronoUnit.MINUTES.between(laterStart, earlierEnd);
                        
                    }
                   
                    System.out.println("" + numberOfOverlappingMinutes + " minutes of overlap");

                    // do total de tempo de sobreaviso, subtraio a intersecção com hora-extra
                    onNoticeIntervalMinutes = onNoticeIntervalMinutes - numberOfOverlappingMinutes;
                }
            }
        }
        Double onNoticeIntervalMinutes_Double = (double)onNoticeIntervalMinutes;
        Double onNoticeIntervalHoursDecimal = onNoticeIntervalMinutes_Double/60;
        return onNoticeIntervalHoursDecimal;
    }

    public static void calculateOverTime(Appointment aptOverTime){

    }
}        
