package org.openjfx.api2semestre.view.controllers.templates;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.utils.AppointmentCalculator;
import org.openjfx.api2semestre.view.utils.ChartGenerator;
import org.openjfx.api2semestre.view.utils.dashboard.DashboardContext;
import org.openjfx.api2semestre.view.utils.dashboard.FilterField;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

public class DashboardTab {
    private DashboardContext context;

    @FXML private HBox hb_filters;
    @FXML private FlowPane fp_charts;

    private ObservableList<Appointment> filteredAppointments;
    private Appointment[] loadedAppointments;

    @SuppressWarnings("unused") private ObservableList<ReportInterval> filteredIntervals;
    private ReportInterval[] loadedIntervals;

    private void createFilters () {

        // double filter_width = hb_filters.getScene().getWindow().getWidth() / (double)context.getFields().length;

        hb_filters.getChildren().addAll(Arrays.asList(context.getFields()).stream()
            .map((FilterField field) -> {
                Control filterControl = field.create(loadedAppointments, loadedIntervals);
                filterControl.setMaxWidth(128);
                return filterControl;
            })
            .collect(Collectors.toList()
        ));
    }
    
    public void setContext(DashboardContext context) {
        this.context = context;
        
        loadedAppointments = context.loadData(Authentication.getCurrentUser());
        loadedIntervals = AppointmentCalculator.calculateReports(loadedAppointments);

        createFilters();

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
                addChart(ChartGenerator.hourIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
            break;
            case Gestor:
            break;
        }

        addChart(ChartGenerator.weekIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
        addChart(ChartGenerator.monthIntersectionCountGraph(filteredAppointments.toArray(Appointment[]::new)));
    }

    private void addChart (Node control) {
        // final double[] SIZES = new double[] {180, 180, 256, 256, 256};
        // control.minWidth(-1);
        // control.prefWidth(-1);
        // control.maxWidth(160);
        fp_charts.getChildren().add(control);
    }

    public Appointment[] getLoadedAppointments() { return loadedAppointments; }
    public ReportInterval[] getLoadedIntervals() { return loadedIntervals; }
}
