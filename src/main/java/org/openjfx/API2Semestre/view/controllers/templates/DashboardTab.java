package org.openjfx.api2semestre.view.controllers.templates;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.appointment.AppointmentType;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.utils.AppointmentCalculator;
import org.openjfx.api2semestre.utils.DateConverter;
import org.openjfx.api2semestre.view.utils.ChartGenerator;
import org.openjfx.api2semestre.view.utils.dashboard.DashboardContext;
import org.openjfx.api2semestre.view.utils.dashboard.FilterControl;
import org.openjfx.api2semestre.view.utils.dashboard.FilterField;

import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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

    FilterControl[] filterControls;

    private void createFilters () {

        // double filter_width = hb_filters.getScene().getWindow().getWidth() / (double)context.getFields().length;

        List<FilterControl> filterControlsList = Arrays.asList(context.getFields()).stream()
            .map((FilterField filterField) -> {
                try {
                    FilterControl filterControl = filterField.create(
                        loadedAppointments,
                        loadedIntervals,
                        this::applyFilter
                    ).orElseThrow(
                        () -> new Exception("Khali | DashboardTab.createFilters -- Error: unhadled FilterField.create() implementation")
                    );
                    return filterControl;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            })
            .filter(filterControl -> filterControl != null)
            .collect(Collectors.toList()
        );

        hb_filters.getChildren().addAll(
            filterControlsList.stream().map(
                (FilterControl filterControl) -> filterControl.getControl()
            ).collect(Collectors.toList())
        );

        filterControls = filterControlsList.toArray(FilterControl[]::new);
    }
    
    public void setContext(DashboardContext context) {
        this.context = context;
        
        // loadedAppointments = context.loadData(Authentication.getCurrentUser());

        loadedAppointments = new Appointment[] {
            new Appointment(1, 0, "123", "Julio",
                AppointmentType.Overtime,
                DateConverter.stringToTimestamp("2023-05-05 12:00:00"), 
                DateConverter.stringToTimestamp("2023-05-05 13:00:00"), 
                0, "Squad Foda", 
                0, "Cleitin", "ProjetoA", 
                "pq sim", 1, "sample"
            ),
            new Appointment(1, 0, "123", "Julio",
                AppointmentType.Overtime,
                DateConverter.stringToTimestamp("2023-05-05 12:30:00"), 
                DateConverter.stringToTimestamp("2023-05-05 13:30:00"), 
                0, "Squad Foda", 
                0, "Cleitin", "ProjetoA", 
                "pq sim", 1, "sample"
            ),
            new Appointment(1, 0, "123", "Julio",
                AppointmentType.OnNotice,
                DateConverter.stringToTimestamp("2023-05-05 13:00:00"), 
                DateConverter.stringToTimestamp("2023-05-05 14:00:00"), 
                0, "Squad Foda", 
                0, "Clovis", "GTA Brasileiro", 
                "pq sim", 0, "sample"
            ),
            new Appointment(1, 0, "123", "Julio",
                AppointmentType.Overtime,
                DateConverter.stringToTimestamp("2023-05-05 13:30:00"), 
                DateConverter.stringToTimestamp("2023-05-05 14:30:00"), 
                0, "Squad Foda", 
                0, "Clovis", "GTA Brasileiro", 
                "pq sim", 2, "sample"
            ),
            new Appointment(1, 0, "123", "Julio",
                AppointmentType.Overtime,
                DateConverter.stringToTimestamp("2023-05-05 14:00:00"), 
                DateConverter.stringToTimestamp("2023-05-05 15:00:00"), 
                0, "Squad Foda", 
                0, "Mr. Klayent", "Porojereto", 
                "pq sim", 0, "sample"
            ),
            new Appointment(1, 0, "123", "Julio",
                AppointmentType.OnNotice,
                DateConverter.stringToTimestamp("2023-05-05 14:15:00"), 
                DateConverter.stringToTimestamp("2023-05-05 14:45:00"), 
                0, "Squad Foda", 
                0, "Mr. Klayent", "Porojereto", 
                "pq sim", 0, "sample"
            )
        };
        loadedIntervals = AppointmentCalculator.calculateReports(loadedAppointments);

        createFilters();

        applyFilter();
    }

    private void applyFilter() {        
        filteredAppointments = FXCollections.observableList(
            Arrays.asList(loadedAppointments).stream()
            .filter((Appointment appointment) -> {
                if (filterControls != null) for (FilterControl filterControl : filterControls) {
                    if (!filterControl.filter(appointment)) return false;
                }
                return true;
            })
            .collect(Collectors.toList())
        );

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
