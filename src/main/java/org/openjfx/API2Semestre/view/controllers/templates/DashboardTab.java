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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;
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
        final ChangeListener<Boolean> applyFilterCallback = new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                applyFilter();
            }
        };

        for (FilterField field : context.getFields()) {

            CheckBox checkbox = new CheckBox(field.getName());

            hb_filters.getChildren().add(checkbox);

            // enableFilterBoolean.addListener(applyFilterCallback);

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
