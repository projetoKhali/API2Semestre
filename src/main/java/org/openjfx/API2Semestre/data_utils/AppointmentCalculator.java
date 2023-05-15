package org.openjfx.api2semestre.data_utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openjfx.api2semestre.appointments.Appointment;
import org.openjfx.api2semestre.appointments.AppointmentType;
import org.openjfx.api2semestre.report.IntervalFee;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.report.Week;

// os cálculos serão feitos em minutos, e depois divididos por 60 para se chegar a quantidade de horas (em decimal).

public class AppointmentCalculator {

    public static List<ReportInterval> calculateReports () {
        List<ReportInterval> reportsFinal = new ArrayList<ReportInterval>();

        List<Appointment> appointments = List.of(
            new Appointment(
                0,
                "Julio", 
                AppointmentType.OnNotice, 
                DateConverter.stringToTimestamp("2023-05-07 22:00:00"), 
                DateConverter.stringToTimestamp("2023-05-08 01:00:00"), 
                "Squad Foda", 
                "Cleitin", 
                "ProjetoA", 
                "pq sim", 
                0, 
                "sample"
            ),
            new Appointment(
                1,
                "Julio", 
                AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-07 22:30:00"), 
                DateConverter.stringToTimestamp("2023-05-07 23:30:00"), 
                "Squad Foda", 
                "Cleitin", 
                "ProjetoA", 
                "pq sim", 
                0, 
                "sample"
            ),
            new Appointment(
                2,
                "Julio", 
                AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-08 16:30:00"), 
                DateConverter.stringToTimestamp("2023-05-08 18:35:00"), 
                "Squad Foda", 
                "Cleitin", 
                "ProjetoA", 
                "pq sim", 
                0, 
                "sample"
            )
        );

        LinkedList<ReportInterval> intervalsOnNotice = new LinkedList<>();

        for(Appointment apt: appointments){
            System.out.println("start time do apontamento: " + apt.getStartDate() + " | end time do apontamento: " + apt.getEndDate());

            LocalDateTime aptStartDateTime = apt.getStartDate().toLocalDateTime();
            LocalDateTime aptEndDateTime = apt.getEndDate().toLocalDateTime();

            double aptTotalTime = ((double) ChronoUnit.MINUTES.between(aptStartDateTime, aptEndDateTime)) / 60;

            if(apt.getType() == AppointmentType.OnNotice){
                System.out.println("start time do sobreaviso: " + apt.getStartDate() + " | end time do sobreaviso: " + apt.getEndDate());
                intervalsOnNotice.add(new ReportInterval(apt.getId(), apt.getStartDate(), apt.getEndDate(), 3016));
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

        for (ReportInterval repInt : calculateOnNotice(intervalsOnNotice, appointments)) reportsFinal.add(repInt);

        return reportsFinal;
    }
    
    public static List<ReportInterval> calculateOnNotice (LinkedList<ReportInterval> intervals, List<Appointment> appointments) {

        // System.out.println("\ncalculateOnNotice");
        // for (ReportInterval i : intervals) {
        //     System.out.println("interval | start: " + i.getStart() + " | end: " + i.getEnd());
        // }
        // for (Appointment a : appointments) {
        //     System.out.println("apt | start: " + a.getStartDate() + " | end: " + a.getEndDate());

        // }

        List<ReportInterval> subIntervals = new ArrayList<>();

        for (int i = 0; i < intervals.size(); i++) {
            ReportInterval currentInterval = intervals.get(i);

            Timestamp onNoticeStart = currentInterval.getStart();
            Timestamp onNoticeEnd = currentInterval.getEnd();

            // System.out.println("onNoticeStart " + onNoticeStart);
            // System.out.println("onNoticeEnd  " + onNoticeEnd);
            
            LocalDateTime onNoticeStartDateTime = onNoticeStart.toLocalDateTime();
            LocalDateTime onNoticeEndDateTime = onNoticeEnd.toLocalDateTime();

            for(Appointment apt : appointments) {
                if (apt.getType() != AppointmentType.Overtime) continue;

                java.sql.Timestamp aptStart = apt.getStartDate();
                java.sql.Timestamp aptEnd = apt.getEndDate();

                LocalDateTime aptStartDateTime = aptStart.toLocalDateTime();
                LocalDateTime aptEndDateTime = aptEnd.toLocalDateTime();

                // Situação A: apt faz instersecção com o fim do sobreaviso
                // apt.start > sobreaviso.end && apt.end <= sobreaviso.end 
                if (
                    aptStartDateTime.isBefore(onNoticeEndDateTime) && 
                    !aptEndDateTime.isBefore(onNoticeEndDateTime) &&
                    aptStartDateTime.isAfter(onNoticeStartDateTime)
                ) {
                    currentInterval.setEnd(aptStart);
                }

                // Situação B: apt faz instersecção com o começo do sobreaviso
                // apt.end > sobreaviso.start && apt.start <= sobreaviso.start 
                else if (
                    aptEndDateTime.isAfter(onNoticeStartDateTime) && 
                    !aptStartDateTime.isAfter(onNoticeStartDateTime) &&
                    aptEndDateTime.isBefore(onNoticeEndDateTime)
                ) {
                    currentInterval.setStart(aptEnd);
                }
                
                // Situação C: apt faz instersecção com o meio do sobreaviso
                // apt.start > sobreaviso.start && apt.end < sobreaviso.end 
                else if (aptStartDateTime.isAfter(onNoticeStartDateTime) && aptEndDateTime.isBefore(onNoticeEndDateTime)) {

                    // Nesse caso, separamos currentInterval em dois. 
                    // Tratamos a primeira metade de acordo com a situação A
                    // currentInterval.setEnd(aptStart);

                    // Cria uma nova lista de sub intervals para calcular as possíveis situações B ou C
                    // da segunda metade de currentInterval. 
                    LinkedList<ReportInterval> temp = new LinkedList<>();
                    temp.add(new ReportInterval(
                        currentInterval.getAppointmmentId(),
                        onNoticeStart,
                        aptStart,
                        3016
                    ));
                    temp.add(new ReportInterval(
                        currentInterval.getAppointmmentId(),
                        aptEnd,
                        onNoticeEnd,
                        3016
                    ));

                    for (ReportInterval subInt : calculateOnNotice(temp, appointments)) subIntervals.add(subInt);

                    intervals.remove(currentInterval);
                    i--;

                    break;
                    
                } 
            }
        }

        List<ReportInterval> intervalsFinal = new ArrayList<>();

        for (int i = subIntervals.size() -1; i >= 0; i--) {
            ReportInterval repInt = subIntervals.get(i);
            if (!intervalsFinal.contains(repInt)) intervalsFinal.add(repInt);
        }
        for (int i = intervals.size() -1; i >= 0; i--) {
            ReportInterval repInt = intervals.get(i);
            if (!intervalsFinal.contains(repInt)) intervalsFinal.add(repInt);
        }

        // System.out.println("\nRETORNO:");
        // for (ReportInterval i : intervals) {
        //     System.out.println("interval | start: " + i.getStart() + " | end: " + i.getEnd());
        // }
        // System.out.println();

        return intervalsFinal;
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

