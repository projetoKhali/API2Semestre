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

public class AppointmentCalculator {
    
    public static List<ReportInAppointment> calculateReports(){
        List<ReportInAppointment> reports = new ArrayList<ReportInAppointment>();
        public List<Appointment> appointments = new ArrayList<Appointment>();
        appointments = Arrays.asList(QueryLibs.collaboratorSelect(Authentication.getCurrentUser().getNome()));

        
        public List<ReportInAppointment> calculateOnNotice(Appointment apt_onNotice){
            java.sql.Timestamp onNotice_init = apt_onNotice.getStartDate();
            java.sql.Timestamp onNotice_end = apt_onNotice.getEndDate();
            java.sql.Timestamp overTime_init = null;
            java.sql.Timestamp overTime_end = null;

            for(Appointment apt: appointments){
                if (apt.getType() == AppointmentType.Overtime){
                    overTime_init = apt.getStartDate();
                    overTime_end = apt.getEndDate();
                    
                    LocalDateTime onotice_init = onNotice_init.toLocalDateTime();
                    LocalDateTime onotice_end = onNotice_end.toLocalDateTime();
                    LocalDateTime overtime_init = overTime_init.toLocalDateTime();
                    LocalDateTime overtime_end = overTime_end.toLocalDateTime();

                    if (onotice_end.isBefore(onotice_init) || overtime_end.isBefore(overtime_init)) {
                        System.out.println("Not proper intervals");
                    } else {
                        long numberOfOverlappingDates;
                        long numberOfOverlappingHours;
                        long numberOfOverlappingMinutes;
                        long numberOfOverlappingSeconds;
                        if (onotice_end.isBefore(overtime_init) || overtime_end.isBefore(onotice_init)) {
                            // no overlap
                            numberOfOverlappingDates = 0;
                            numberOfOverlappingHours = 0;
                            numberOfOverlappingMinutes = 0;
                            numberOfOverlappingSeconds = 0;
                        } else {
                            LocalDateTime laterStart = Collections.max(Arrays.asList(onotice_init, overtime_init));
                            LocalDateTime earlierEnd = Collections.min(Arrays.asList(onotice_end, overtime_end));
                            numberOfOverlappingDates = ChronoUnit.DAYS.between(laterStart, earlierEnd);
                            numberOfOverlappingHours = ChronoUnit.HOURS.between(laterStart, earlierEnd);
                            numberOfOverlappingMinutes = ChronoUnit.MINUTES.between(laterStart, earlierEnd);
                            numberOfOverlappingSeconds = ChronoUnit.SECONDS.between(laterStart, earlierEnd);
                        }
                        System.out.println("" + numberOfOverlappingDates + " days of overlap");
                        System.out.println("" + numberOfOverlappingHours + " hour of overlap");
                        System.out.println("" + numberOfOverlappingMinutes + " minutes of overlap");
                        System.out.println("" + numberOfOverlappingSeconds + " seconds of overlap");
                    }
                            


                }

            }

        


        }

        
    }
    
    
}
