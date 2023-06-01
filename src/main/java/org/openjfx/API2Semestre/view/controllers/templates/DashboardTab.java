package org.openjfx.api2semestre.view.controllers.templates;

import java.util.Arrays;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.view.utils.ChartGenerator;
import org.openjfx.api2semestre.view.utils.dashboard.DashboardContext;
import org.openjfx.api2semestre.view.utils.wrappers.ReportIntervalWrapper;

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

    private ObservableList<ReportIntervalWrapper> filteredIntervals;
    private ReportInterval[] loadedIntervals;

    public void initialize() {
        System.out.println("DashboardTab");

        createFilters();
    }

    private void createFilters () {
        
    }
    
    public void setContext(DashboardContext context) {
        this.context = context;

        loadedAppointments = context.loadData(Authentication.getCurrentUser());
        filteredAppointments = FXCollections.observableList(Arrays.asList(loadedAppointments));

        updateCharts();

    }

    private void applyFilter() {

    }

    private void updateCharts () {
        fp_charts.getChildren().add(ChartGenerator.hourIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
        fp_charts.getChildren().add(ChartGenerator.weekIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
        fp_charts.getChildren().add(ChartGenerator.monthIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
    }

}
