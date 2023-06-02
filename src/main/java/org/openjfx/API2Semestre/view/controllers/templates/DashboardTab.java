package org.openjfx.api2semestre.view.controllers.templates;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.utils.AppointmentCalculator;
import org.openjfx.api2semestre.view.utils.ChartGenerator;
import org.openjfx.api2semestre.view.utils.dashboard.DashboardContext;
import org.openjfx.api2semestre.view.utils.dashboard.FilterField;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class DashboardTab {
    private DashboardContext context;

    @FXML private HBox hb_filters;
    @FXML private FlowPane fp_charts;

    private ObservableList<Appointment> filteredAppointments;
    private Appointment[] loadedAppointments;

    private ObservableList<ReportInterval> filteredIntervals;
    private ReportInterval[] loadedIntervals;

    private void createFilters () {
        for (FilterField field : context.getFields()) {

            // load template 
            field.getName(); // field name

            // set filter boolean
            
            // set applyFilter callback
        }
    }
    
    public void setContext(DashboardContext context) {
        this.context = context;

        createFilters();

        loadedAppointments = context.loadData(Authentication.getCurrentUser());
        loadedIntervals = AppointmentCalculator.calculateReports(loadedAppointments);

        applyFilter();
    }

    private void applyFilter() {        
        filteredAppointments = FXCollections.observableList(Arrays.asList(loadedAppointments));
        filteredIntervals = FXCollections.observableList(
            Arrays.asList(loadedIntervals).stream().filter((ReportInterval interval) -> {
                for (Appointment apt : filteredAppointments) {
                    if (interval.getAppointmmentId() == apt.getId()) return true;
                }
                return false;
            }).collect(Collectors.toList())
        );

        updateCharts();

    }

    private void updateCharts () {
        fp_charts.getChildren().clear();

        switch (context.getProfile()) {
            case Administrator: ; 

            break;
            case Colaborador: ; 
                fp_charts.getChildren().add(ChartGenerator.hourIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
            break;
            case Gestor:
            break;
        }

        fp_charts.getChildren().add(ChartGenerator.weekIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
        fp_charts.getChildren().add(ChartGenerator.monthIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
    }

}
