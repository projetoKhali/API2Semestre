package org.openjfx.api2semestre.view.controllers.views;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.appointment.AppointmentType;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.data.ResultCenter;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.utils.AppointmentCalculator;
import org.openjfx.api2semestre.utils.DateConverter;
import org.openjfx.api2semestre.view.utils.ChartGenerator;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class Dashboard {

    @FXML private HBox hb_filters;

    @FXML private FlowPane fp_charts;

    public void initialize() {


        Appointment[] appointments = new Appointment[] {
            // new Appointment(1, "Julio", AppointmentType.Overtime, 
            //     DateConverter.stringToTimestamp("2023-05-05 22:00:00"), 
            //     DateConverter.stringToTimestamp("2023-05-07 19:00:00"), 
            //     "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            // ),
            // new Appointment(1, "Julio", AppointmentType.Overtime, 
            //     DateConverter.stringToTimestamp("2023-05-05 22:00:00"), 
            //     DateConverter.stringToTimestamp("2023-05-06 01:00:00"), 
            //     "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            // ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 12:00:00"), 
                DateConverter.stringToTimestamp("2023-05-05 13:00:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 1, "sample"
            ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 12:30:00"), 
                DateConverter.stringToTimestamp("2023-05-05 13:30:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 1, "sample"
            ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 13:00:00"), 
                DateConverter.stringToTimestamp("2023-05-05 14:00:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 13:30:00"), 
                DateConverter.stringToTimestamp("2023-05-05 14:30:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 2, "sample"
            ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 14:00:00"), 
                DateConverter.stringToTimestamp("2023-05-05 15:00:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            ),
            new Appointment(1, "Julio", AppointmentType.Overtime, 
                DateConverter.stringToTimestamp("2023-05-05 14:15:00"), 
                DateConverter.stringToTimestamp("2023-05-05 14:45:00"), 
                "Squad Foda", "Cleitin", "ProjetoA", "pq sim", 0, "sample"
            )
        };
        
        fp_charts.getChildren().add(ChartGenerator.statusAppointmentChart(appointments));

        List<Appointment> listAppointments = new ArrayList<>();
        List<User> listUsers = new ArrayList<>();

        // se o usuáirio logado for um administrador, retornar uma lista com todos os apontamentos do sistema
        if (Authentication.getCurrentUser().getProfile() == Profile.Administrator) {
            listAppointments = Arrays.asList(QueryLibs.selectAllAppointments());
            Appointment[] appointmentArray = listAppointments.toArray(new Appointment[0]);
            List<ReportInterval> reportIntervals = AppointmentCalculator.calculateReports(appointmentArray);
        }
        // se o usuário logado for um colaborador, retornar uma lista com todos os apontamentos do usuário logado
        else if (Authentication.getCurrentUser().getProfile() == Profile.Colaborador) {
            listAppointments = Arrays.asList(QueryLibs.selectAppointmentsByUser(Authentication.getCurrentUser().getId()));
            Appointment[] appointmentArray = listAppointments.toArray(new Appointment[0]);
            List<ReportInterval> reportIntervals = AppointmentCalculator.calculateReports(appointmentArray);
        }
        // se o usuário logado for um gestor, retornar uma lista com todos os apontamentos dos colaboradores administradores
        else if (Authentication.getCurrentUser().getProfile() == Profile.Gestor) {
            // cria uma lista com todas as squads que o gestor faz parte
            ResultCenter[] listResultCenters = QueryLibs.selectAllResultCentersOfUser(Authentication.getCurrentUser().getId());
        
            // cria uma lista com todos os usuários que estão na squads criadas anteriormente
            for (ResultCenter resultCenter : listResultCenters) {
                User[] crUsers = QueryLibs.selectAllUsersInResultCenter(resultCenter.getId());
                listUsers.addAll(Arrays.asList(crUsers));
            }
        
            // cria uma lista com todos os apontamentos dos usuários criados anteriormente
            for (User user : listUsers) {
                Appointment[] userAppointments = QueryLibs.selectAppointmentsByUser(user.getId());
                listAppointments.addAll(Arrays.asList(userAppointments));
            }
        
            // cria um array com todos os apontamentos e reportIntervals
            Appointment[] appointmentArray = listAppointments.toArray(new Appointment[0]);
            List<ReportInterval> reportIntervals = AppointmentCalculator.calculateReports(appointmentArray);
        }
    }
}