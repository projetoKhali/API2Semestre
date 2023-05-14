package org.openjfx.api2semestre.data_utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.openjfx.api2semestre.appointments.Appointment;
import org.openjfx.api2semestre.appointments.AppointmentType;
import org.openjfx.api2semestre.report.IntervalFee;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.report.Week;

// os cálculos serão feitos em minutos, e depois divididos por 60 para se chegar a quantidade de horas (em decimal).

public class AppointmentCalculator {
    
    static List<ReportInAppointment> reports = new ArrayList<ReportInAppointment>();
    static List<Appointment> appointments = new ArrayList<Appointment>();
    static Double aptTotalTime;
  
    
    public static List<ReportInterval> calculateReports () {
        List<ReportInterval> reportsFinal = new ArrayList<ReportInterval>();

        List<Appointment> appointments = List.of(
            new Appointment(
                1, 
                "Julio", 
                AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 22:00:00"), 
                DateConverter.stringToTimestamp("2023-05-07 19:00:00"), 
                "Squad Foda", 
                "Cleitin", 
                "ProjetoA", 
                "pq sim", 
                0, 
                "sample"
            )
        );

        for(Appointment apt: appointments){
            System.out.println("start time do apontamento: " + apt.getStartDate() + " | end time do apontamento: " + apt.getEndDate());

            LocalDateTime aptStartDateTime = apt.getStartDate().toLocalDateTime();
            LocalDateTime aptEndDateTime = apt.getEndDate().toLocalDateTime();

            aptTotalTime = ((double) ChronoUnit.MINUTES.between(aptStartDateTime, aptEndDateTime)) / 60;

            if(apt.getType() == AppointmentType.OnNotice){
                System.out.println("start time do sobreaviso: " + apt.getStartDate() + " | end time do sobreaviso: " + apt.getEndDate());
                for(ReportInterval repInt: calculateOnNotice(apt)) reportsFinal.add(repInt);
            }
            else {

                System.out.println("é hora extra");

                for(IntervalFee verba: IntervalFee.VERBAS){

                    if(verba.getCode() == 1809 || Week.FDS.compare(verba.getDaysOfWeek())) {
                        for(ReportInterval repInt : calculateIntervals(apt, verba)) reportsFinal.add(repInt);
                    }
                    
                    else if (verba.getMinHourCount() != 0){
                        if (aptTotalTime <= 2) continue;
                        Appointment aptLastHours = apt.copy();
                        aptLastHours.setStartDate(DateConverter.toTimestamp((apt.getStartDate().toLocalDateTime()).plusHours(2)));
                        for(ReportInterval repInt : calculateIntervals(aptLastHours, verba)) reportsFinal.add(repInt);
                    }

                    else{
                        if (aptTotalTime > 2) {
                            Appointment aptFirstHours = apt.copy();
                            aptFirstHours.setEndDate(DateConverter.toTimestamp((apt.getStartDate().toLocalDateTime()).plusHours(2)));
                            List<ReportInterval> reportsTemporary = calculateIntervals(aptFirstHours, verba);
                            for(ReportInterval repInt: reportsTemporary) reportsFinal.add(repInt);
                        } else {
                            List<ReportInterval> reportsTemporary = calculateIntervals(apt, verba);
                            for(ReportInterval repInt: reportsTemporary) reportsFinal.add(repInt);
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
            System.out.println("onNotice_init " + onNotice_init);
            System.out.println("onNotice_end " + onNotice_end);
        }
        // Double onNoticeIntervalMinutes_Double = (double)onNoticeIntervalMinutes;
        // Double onNoticeIntervalHoursDecimal = onNoticeIntervalMinutes_Double/60;
        // return onNoticeIntervalHoursDecimal;
        return reportsOnNotice;

    }


    private static boolean detectaInterDiaSemana (IntervalFee intervalFee, LocalDate aptStartLocalDate, LocalDate aptEndLocalDate) {
        LocalDate actualDay = aptStartLocalDate;
        while(!actualDay.isAfter(aptEndLocalDate)){
            int actualDayOfWeek = actualDay.getDayOfWeek().getValue();
            // uso %7 pois getDayOfWeek().getValue() considera domingo como sendo 7, e para padronizar quero que seja 0
            if(intervalFee.getDaysOfWeek()[(actualDayOfWeek % 7)]) return true;
            actualDay = actualDay.plusDays(1);
            actualDayOfWeek = actualDay.getDayOfWeek().getValue();
        }
        return false;
    }

    public static List<ReportInterval> calculateIntervals(Appointment aptOverTime, IntervalFee intervalFee){

        List<ReportInterval> reportsOvertime = new ArrayList<ReportInterval>();
            
        LocalDateTime aptStartDateTime = aptOverTime.getStartDate().toLocalDateTime();
        LocalDate aptStartLocalDate = aptStartDateTime.toLocalDate();
        
        LocalDateTime aptEndDateTime = aptOverTime.getEndDate().toLocalDateTime();
        LocalDate aptEndLocalDate = aptEndDateTime.toLocalDate();
        
        if (!detectaInterDiaSemana(intervalFee, aptStartLocalDate, aptEndLocalDate)) return List.of();

        if((intervalFee.getStartHour() != null) && (intervalFee.getEndHour() != null)){
            for (LocalDate actualDay = aptStartLocalDate; !actualDay.isAfter(aptEndLocalDate); actualDay = actualDay.plusDays(1)) {
                System.out.println("actualDay: " + actualDay.toString() + " | start: " + aptStartLocalDate + " | !actualDay.isAfter(" + aptEndLocalDate + ")? " + !actualDay.isAfter(aptEndLocalDate));

                int actualDayOfWeek = actualDay.getDayOfWeek().getValue();

                if(intervalFee.getDaysOfWeek()[actualDayOfWeek % 7]) {

                    LocalDateTime verbastart = actualDay.atTime(intervalFee.getStartHour());
                    LocalDateTime verbaEnd = null;

                    // situação em que a hora termina no dia seguinte
                    if(intervalFee.getEndHour().isBefore(intervalFee.getStartHour())){
                        if(actualDay == aptStartLocalDate || (Week.FDS.compare(intervalFee.getDaysOfWeek()) && actualDayOfWeek % 7 == 6)){

                            System.out.println("verba: " + intervalFee.getCode() + " | actualDay: " + actualDay + " == aptStartLocalDate: " + aptStartLocalDate);
                            LocalDateTime verbaStart_ = actualDay.atTime(0, 0);
                            LocalDateTime verbaEnd_ = actualDay.atTime(intervalFee.getEndHour());

                            if(!verbaStart_.isAfter(aptEndDateTime) && !aptStartDateTime.isAfter(verbaEnd_)) {

                                LocalDateTime laterStart_ = Collections.max(Arrays.asList(verbaStart_, aptStartDateTime));
                                LocalDateTime earlierEnd_ = Collections.min(Arrays.asList(verbaEnd_, aptEndDateTime));
            
                                reportsOvertime.add(new ReportInterval(
                                    aptOverTime.getId(), 
                                    DateConverter.toTimestamp(laterStart_), 
                                    DateConverter.toTimestamp(earlierEnd_),
                                    intervalFee.getCode()
                                ));
                            }
                        }

                        // se no dia seguinte a verba é válida
                        if(intervalFee.getDaysOfWeek()[(actualDayOfWeek+1) % 7]){
                            verbaEnd = (actualDay.plusDays(1)).atTime(intervalFee.getEndHour());
                        }

                        // se no dia seguinte a verba não está ativa
                        else verbaEnd = actualDay.plusDays(1).atTime(0,0);
                        
                    }

                    // situação em que a hora começa e termina no mesmo dia
                    else verbaEnd = actualDay.atTime(intervalFee.getEndHour());

                    if (!verbastart.isAfter(aptEndDateTime) && !aptStartDateTime.isAfter(verbaEnd)) {
                        
                        LocalDateTime laterStart = Collections.max(Arrays.asList(verbastart, aptStartDateTime));
                        LocalDateTime earlierEnd = Collections.min(Arrays.asList(verbaEnd, aptEndDateTime));
    
                        reportsOvertime.add(new ReportInterval(
                            aptOverTime.getId(), 
                            DateConverter.toTimestamp(laterStart), 
                            DateConverter.toTimestamp(earlierEnd),
                            intervalFee.getCode()
                        ));
                    }

                }
            }
        }

        return reportsOvertime;
    }

}

