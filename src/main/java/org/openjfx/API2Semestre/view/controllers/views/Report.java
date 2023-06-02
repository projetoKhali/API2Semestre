package org.openjfx.api2semestre.view.controllers.views;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.report.ReportExporter;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.utils.AppointmentCalculator;
import org.openjfx.api2semestre.view.macros.ColumnConfig;
import org.openjfx.api2semestre.view.macros.ColumnConfigString;
import org.openjfx.api2semestre.view.macros.TableMacros;
import org.openjfx.api2semestre.view.utils.filters.IntervalFilter;
import org.openjfx.api2semestre.view.utils.wrappers.ReportIntervalWrapper;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class Report {

    @FXML private TableView<ReportIntervalWrapper> tabela;

    @FXML private TableColumn<ReportIntervalWrapper, String> col_matricula;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_colaborador;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_verba;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_inicio;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_fim;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_total;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_cr;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_cliente;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_projeto;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_justificativa;
    
    @FXML private CheckBox cb_matricula;
    @FXML private CheckBox cb_colaborador;
    @FXML private CheckBox cb_verba;
    @FXML private CheckBox cb_inicio;
    @FXML private CheckBox cb_fim;
    @FXML private CheckBox cb_total;
    @FXML private CheckBox cb_cr;
    @FXML private CheckBox cb_cliente;
    @FXML private CheckBox cb_projeto;
    @FXML private CheckBox cb_justificativa;

    private BooleanProperty col_matricula_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_colaborador_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_verba_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_cr_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_cliente_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_projeto_enableFilter = new SimpleBooleanProperty();

    private ObservableList<ReportIntervalWrapper> intervalsToExport;
    private ReportInterval[] loadedIntervals;
    
    public void initialize() {

        // System.out.println("oi");
        
        buildCheckBoxes();
        // System.out.println("buildCheckBoxes() done!");
        
        buildTable();
        // System.out.println("buildTable() done!");
        
        updateTable();
        // System.out.println("updateTable() done!");

    }

    private void buildCheckBoxes () {
        cb_matricula.setSelected(true);
        cb_colaborador.setSelected(true);
        cb_verba.setSelected(true);
        cb_inicio.setSelected(true);
        cb_fim.setSelected(true);
        cb_total.setSelected(true);
        cb_cr.setSelected(true);
        cb_cliente.setSelected(true);
        cb_projeto.setSelected(true);
        cb_justificativa.setSelected(true);
        cb_matricula.setOnAction(event -> handleCheckBoxAction(cb_matricula, col_matricula));
        cb_colaborador.setOnAction(event -> handleCheckBoxAction(cb_colaborador, col_colaborador));
        cb_verba.setOnAction(event -> handleCheckBoxAction(cb_verba, col_verba));
        cb_inicio.setOnAction(event -> handleCheckBoxAction(cb_inicio, col_inicio));
        cb_fim.setOnAction(event -> handleCheckBoxAction(cb_fim, col_fim));
        cb_total.setOnAction(event -> handleCheckBoxAction(cb_total, col_total));
        cb_cr.setOnAction(event -> handleCheckBoxAction(cb_cr, col_cr));
        cb_cliente.setOnAction(event -> handleCheckBoxAction(cb_cliente, col_cliente));
        cb_projeto.setOnAction(event -> handleCheckBoxAction(cb_projeto, col_projeto));
        cb_justificativa.setOnAction(event -> handleCheckBoxAction(cb_justificativa, col_justificativa));
    }

    private <T> void handleCheckBoxAction(CheckBox checkBox, TableColumn<ReportIntervalWrapper, T> column) {
        boolean selected = checkBox.isSelected();
        column.setResizable(selected);
        column.setVisible(selected);
    }

    @SuppressWarnings("unchecked")
    private void buildTable () {

        ChangeListener<Boolean> applyFilterCallback = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                applyFilter();
            }
        };

        TableMacros.buildTable(
            tabela,
            new ColumnConfig[] {
                new ColumnConfigString<>(col_matricula, "requesterRegistration", "Matricula", Optional.of(col_matricula_enableFilter)),
                new ColumnConfigString<>(col_colaborador, "requesterName", "Colaborador", Optional.of(col_colaborador_enableFilter)),
                new ColumnConfigString<>(col_verba, "intervalFeeCode", "Verba", Optional.of(col_verba_enableFilter)),
                new ColumnConfigString<>(col_cr, "resultCenterName", "CR", Optional.of(col_cr_enableFilter)),
                new ColumnConfigString<>(col_cliente, "clientName", "Cliente", Optional.of(col_cliente_enableFilter)),
                new ColumnConfigString<>(col_projeto, "projectName", "Projeto", Optional.of(col_projeto_enableFilter)),
            },
            Optional.of(applyFilterCallback)
        );

        col_inicio.setCellValueFactory(new PropertyValueFactory<>("horaInício"));
        col_fim.setCellValueFactory(new PropertyValueFactory<>("horaFim"));
        col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        col_justificativa.setCellValueFactory(new PropertyValueFactory<>("justificativa"));
    }

    private void updateTable () {
        loadedIntervals = AppointmentCalculator.calculateReports(QueryLibs.selectAllAppointments());
        System.out.println(loadedIntervals.length + " intervals");
        applyFilter();
    }

    private void applyFilter () {
        System.out.println("applyFilter");

        intervalsToExport = FXCollections.observableArrayList(IntervalFilter.filterFromView(
            Arrays.asList(loadedIntervals).stream().map(interval -> {
                System.out.println("Loading appointment of id " + interval.getAppointmmentId());
                return new ReportIntervalWrapper(
                    org.openjfx.api2semestre.database.QueryLibs.selectAppointmentById(interval.getAppointmmentId()).get(),
                    interval
                );
            }).collect(Collectors.toList()),
            col_matricula_enableFilter.get() ? Optional.of(col_matricula) : Optional.empty(),
            col_colaborador_enableFilter.get() ? Optional.of(col_colaborador) : Optional.empty(),
            col_verba_enableFilter.get() ? Optional.of(col_verba) : Optional.empty(),
            col_cr_enableFilter.get() ? Optional.of(col_cr) : Optional.empty(),
            col_cliente_enableFilter.get() ? Optional.of(col_cliente) : Optional.empty(),
            col_projeto_enableFilter.get() ? Optional.of(col_projeto) : Optional.empty()
        ));

        tabela.setItems(intervalsToExport);
        tabela.refresh();
    }

    @FXML public void export (ActionEvent e) {
        String local = ReportExporter.showSaveDialog(App.getStage());
        ReportExporter.exporterCSV(loadedIntervals, local); // TODO: Use filtered Intervals
    }
}
