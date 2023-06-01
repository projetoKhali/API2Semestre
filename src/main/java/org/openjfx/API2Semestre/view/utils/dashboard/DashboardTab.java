package org.openjfx.api2semestre.view.utils.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.data.ResultCenter;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.utils.AppointmentCalculator;
import org.openjfx.api2semestre.view.utils.ChartGenerator;
import org.openjfx.api2semestre.view.utils.wrappers.ReportIntervalWrapper;

import javafx.collections.ObservableList;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class DashboardTab {
    private HBox hb_filters;
    private FlowPane fp_charts;

    private ObservableList<Appointment> filteredAppointments;
    private Appointment[] loadedAppointments;

    private ObservableList<ReportIntervalWrapper> filteredIntervals;
    private ReportInterval[] loadedIntervals;

    public DashboardTab(HBox hb_filters, FlowPane fp_charts) {
        this.hb_filters = hb_filters;
        this.fp_charts = fp_charts;
    }

    public void initialize() {

        createFilters();

        loadData();

        updateCharts();

    }

    private void createFilters () {
        
    }

    private void loadData () {

        User currentUser = Authentication.getCurrentUser();

        // se o usuário logado for um administrador, retornar uma lista com todos os apontamentos do sistema
        if (currentUser.getProfile().getProfileLevel() >= Profile.Administrator.getProfileLevel()) {
            loadedAppointments = QueryLibs.selectAllAppointments();
            loadedIntervals = AppointmentCalculator.calculateReports(loadedAppointments);
        }
        // se o usuário logado for um colaborador, retornar uma lista com todos os apontamentos do usuário logado
        else if (currentUser.getProfile().getProfileLevel() >= Profile.Colaborador.getProfileLevel()) {
            loadedAppointments = QueryLibs.selectAppointmentsByUser(currentUser.getId());
            loadedIntervals = AppointmentCalculator.calculateReports(loadedAppointments);
        }
        // se o usuário logado for um gestor, retornar uma lista com todos os apontamentos dos colaboradores administradores
        else if (currentUser.getProfile().getProfileLevel() >= Profile.Gestor.getProfileLevel()) {
            // cria uma lista com todas as squads que o gestor faz parte
            ResultCenter[] listResultCenters = QueryLibs.selectAllResultCentersOfUser(currentUser.getId());
        
            List<Appointment> listAppointments = new ArrayList<>();
            List<User> loadedUsers = new ArrayList<>();
    
            // cria uma lista com todos os usuários que estão na squads criadas anteriormente
            for (ResultCenter resultCenter : listResultCenters) {
                User[] crUsers = QueryLibs.selectAllUsersInResultCenter(resultCenter.getId());
                loadedUsers.addAll(Arrays.asList(crUsers));
            }
        
            // cria uma lista com todos os apontamentos dos usuários criados anteriormente
            for (User user : loadedUsers) {
                Appointment[] userAppointments = QueryLibs.selectAppointmentsByUser(user.getId());
                listAppointments.addAll(Arrays.asList(userAppointments));
            }
        
            // cria um array com todos os apontamentos e reportIntervals
            loadedAppointments = listAppointments.toArray(Appointment[]::new);
            loadedIntervals = AppointmentCalculator.calculateReports(loadedAppointments);

        }
    }

    private void applyFilter() {

    }

    private void updateCharts () {
        fp_charts.getChildren().add(ChartGenerator.hourIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
        fp_charts.getChildren().add(ChartGenerator.weekIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
        fp_charts.getChildren().add(ChartGenerator.monthIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
    }

}
