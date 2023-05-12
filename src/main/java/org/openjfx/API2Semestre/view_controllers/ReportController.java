package org.openjfx.api2semestre.view_controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.data_utils.DateConverter;
import org.openjfx.api2semestre.report.ReportExporter;
import org.openjfx.api2semestre.report.ReportInterval;
import org.openjfx.api2semestre.view_macros.ColumnConfig;
import org.openjfx.api2semestre.view_macros.ColumnConfigString;
import org.openjfx.api2semestre.view_macros.TableMacros;
import org.openjfx.api2semestre.view_utils.IntervalFilter;
import org.openjfx.api2semestre.view_utils.ReportIntervalWrapper;

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

public class ReportController {

    @FXML private TableView<ReportIntervalWrapper> tabela;

    @FXML private TableColumn<ReportIntervalWrapper, String> col_matricula;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_colaborador;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_verba;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_inicio;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_fim;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_cr;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_cliente;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_projeto;
    @FXML private TableColumn<ReportIntervalWrapper, String> col_justificativa;
    
    @FXML private CheckBox cb_matricula;
    @FXML private CheckBox cb_colaborador;
    @FXML private CheckBox cb_verba;
    @FXML private CheckBox cb_inicio;
    @FXML private CheckBox cb_fim;
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
    private List<ReportInterval> loadedIntervals;
    
    public void initialize() {

        System.out.println("oi");
        
        buildCheckBoxes();
        System.out.println("buildCheckBoxes() done!");
        
        buildTable();
        System.out.println("buildTable() done!");
        
        updateTable();
        System.out.println("updateTable() done!");

    }

    private void buildCheckBoxes () {
        cb_matricula.setSelected(true);
        cb_colaborador.setSelected(true);
        cb_verba.setSelected(true);
        cb_inicio.setSelected(true);
        cb_fim.setSelected(true);
        cb_cr.setSelected(true);
        cb_cliente.setSelected(true);
        cb_projeto.setSelected(true);
        cb_justificativa.setSelected(true);
        cb_matricula.setOnAction(event -> handleCheckBoxAction(cb_matricula, col_matricula));
        cb_colaborador.setOnAction(event -> handleCheckBoxAction(cb_colaborador, col_colaborador));
        cb_verba.setOnAction(event -> handleCheckBoxAction(cb_verba, col_verba));
        cb_inicio.setOnAction(event -> handleCheckBoxAction(cb_inicio, col_inicio));
        cb_fim.setOnAction(event -> handleCheckBoxAction(cb_fim, col_fim));
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
                new ColumnConfigString<>(col_matricula, "matricula", "Matricula", Optional.of(col_matricula_enableFilter)),
                new ColumnConfigString<>(col_colaborador, "colaborador", "Colaborador", Optional.of(col_colaborador_enableFilter)),
                new ColumnConfigString<>(col_verba, "verba", "Verba", Optional.of(col_verba_enableFilter)),
                new ColumnConfigString<>(col_cr, "centroResultado", "CR", Optional.of(col_cr_enableFilter)),
                new ColumnConfigString<>(col_cliente, "cliente", "Cliente", Optional.of(col_cliente_enableFilter)),
                new ColumnConfigString<>(col_projeto, "projeto", "Projeto", Optional.of(col_projeto_enableFilter)),
            },
            Optional.of(applyFilterCallback)
        );
    }

    private void updateTable () {
        loadedIntervals = List.of(
            new ReportInterval(1,DateConverter.inputToTimestamp(LocalDate.of(12, 12, 12),"12:12"),DateConverter.inputToTimestamp(LocalDate.of(11, 11, 11),"11:11"), 12345),
            new ReportInterval(1,DateConverter.inputToTimestamp(LocalDate.of(12, 12, 12),"10:12"),DateConverter.inputToTimestamp(LocalDate.of(11, 11, 11),"11:11"), 12345),
            new ReportInterval(1,DateConverter.inputToTimestamp(LocalDate.of(12, 12, 12),"12:12"),DateConverter.inputToTimestamp(LocalDate.of(11, 11, 11),"11:11"), 12345),
            new ReportInterval(1,DateConverter.inputToTimestamp(LocalDate.of(12, 12, 12),"12:12"),DateConverter.inputToTimestamp(LocalDate.of(11, 11, 11),"11:11"), 12345),
            new ReportInterval(1,DateConverter.inputToTimestamp(LocalDate.of(12, 12, 12),"12:12"),DateConverter.inputToTimestamp(LocalDate.of(11, 11, 11),"11:11"), 12345)
        );
        applyFilter();
    }

    private void applyFilter () {
        System.out.println("applyFilter");

        intervalsToExport = FXCollections.observableArrayList(IntervalFilter.filterFromView(
            loadedIntervals.stream().map(interval -> {
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
