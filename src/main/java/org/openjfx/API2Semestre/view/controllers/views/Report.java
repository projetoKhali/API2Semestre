package org.openjfx.api2semestre.view.controllers.views;

// import java.security.Timestamp;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.data.Expedient;
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
import javafx.scene.control.DatePicker;
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

    @FXML private DatePicker date_Inicio = new DatePicker(null);
    @FXML private DatePicker date_Fim = new DatePicker(null);
    
    @FXML private CheckBox cb_fechaAtual;
    @FXML private CheckBox cb_fechaAnterior;
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
    private List<ReportInterval> loadedIntervals;
    
    public void initialize() {

        // System.out.println("oi");
        buildCheckBoxesPeriod();
        
        buildCheckBoxes();
        // System.out.println("buildCheckBoxes() done!");
        
        buildTable();
        // System.out.println("buildTable() done!");
        
        updateTable();
        // System.out.println("updateTable() done!");

    }

    private void choose_period(){
        if(cb_fechaAtual.isSelected() || cb_fechaAnterior.isSelected()){
                        date_Inicio.setDisable(true);
                        date_Fim.setDisable(true);
                    }
        else{date_Inicio.setDisable(false); date_Fim.setDisable(false);}
        // cb_fechaAnterior.selectedProperty().addListener((observable, oldValue, newValue) -> {
        //     date_Inicio.setDisable(newValue);
        //     date_Fim.setDisable(newValue);
        // });
        // cb_fechaAtual.selectedProperty().addListener((observable, oldValue, newValue) -> {
        //     date_Inicio.setDisable(newValue);
        //     date_Fim.setDisable(newValue);
        // });

        // ChangeListener<Boolean> applyFilterCallbackPeriod = new ChangeListener<Boolean>() {
        //     @Override
        //     public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
        //         if(cb_fechaAtual.isSelected() || cb_fechaAnterior.isSelected()){
        //             date_Inicio.setDisable(true);
        //             date_Fim.setDisable(true);
        //         }
        //     }
        // };
            
        
    }
    private void buildCheckBoxesPeriod(){
        cb_fechaAtual.setSelected(true);
        cb_fechaAnterior.setSelected(true);
        date_Inicio.setDisable(true);
        date_Fim.setDisable(true);
        cb_fechaAtual.setOnAction(event -> choose_period());
        cb_fechaAnterior.setOnAction(event -> choose_period());
        
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
                new ColumnConfigString<>(col_matricula, "matricula", "Matricula", Optional.of(col_matricula_enableFilter)),
                new ColumnConfigString<>(col_colaborador, "colaborador", "Colaborador", Optional.of(col_colaborador_enableFilter)),
                new ColumnConfigString<>(col_verba, "verba", "Verba", Optional.of(col_verba_enableFilter)),
                new ColumnConfigString<>(col_cr, "centroResultado", "CR", Optional.of(col_cr_enableFilter)),
                new ColumnConfigString<>(col_cliente, "cliente", "Cliente", Optional.of(col_cliente_enableFilter)),
                new ColumnConfigString<>(col_projeto, "projeto", "Projeto", Optional.of(col_projeto_enableFilter)),
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
        System.out.println(loadedIntervals.size() + " intervals");
        applyFilter();
    }

    // filtro de escrever
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
        Boolean[] selectedItens = new Boolean[]{
            cb_matricula.isSelected(),
            cb_colaborador.isSelected(),
            cb_verba.isSelected(),
            cb_inicio.isSelected(),
            cb_fim.isSelected(),
            cb_total.isSelected(),
            cb_cr.isSelected(),
            cb_cliente.isSelected(),
            cb_projeto.isSelected(),
            cb_justificativa.isSelected()
        };

        Integer dia_inicio;
        Integer dia_fim;
        Timestamp data_inicio;
        Timestamp data_fim;

        LocalDate hoje = LocalDate.now();
        Integer mesAtual = hoje.getMonthValue();
        int anoAtual = hoje.getYear();

        data_inicio = Timestamp.valueOf((hoje.atTime(23, 59, 59)).minusMonths(6));
        data_fim = Timestamp.valueOf(hoje.atTime(23, 59, 59));
        
        if(cb_fechaAtual.isSelected() & cb_fechaAnterior.isSelected()){
            dia_fim = Expedient.getClosingDay();
            dia_inicio = dia_fim + 1;
            if(hoje.getDayOfMonth() >= dia_inicio){
                data_inicio = Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(1));
            }
            else{
                data_inicio = Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(2));
            }
            data_fim = Timestamp.valueOf(hoje.atTime(23, 59, 59));
            
        }
        else if(cb_fechaAtual.isSelected()){
            dia_fim = Expedient.getClosingDay();
            dia_inicio = dia_fim + 1;
            if(hoje.getDayOfMonth() >= dia_inicio){
                data_inicio = Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()));
            }
            else{
                data_inicio = Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(1));
            }
            data_fim = Timestamp.valueOf(hoje.atTime(23, 59, 59));
            
        }
        else if(cb_fechaAnterior.isSelected()){
            dia_fim = Expedient.getClosingDay();
            dia_inicio = dia_fim + 1;
            if(hoje.getDayOfMonth() >= dia_inicio){
                if(hoje.getDayOfMonth()!= 1){
                    data_inicio = Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(1));
                    data_fim = Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_fim).atTime(23, 59, 59)));
                }
                else{
                    data_inicio = Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(1));
                    data_fim = Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_fim).atTime(23, 59, 59)).minusMonths(1));

                }
            }
            else{
                data_inicio = Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(2));
                data_fim = Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_fim).atTime(23, 59, 59)).minusMonths(1));

            }
        }

        else{
            if(date_Inicio.getValue() != null && date_Fim.getValue() != null){
                data_inicio = Timestamp.valueOf(date_Inicio.getValue().atStartOfDay());
                data_fim = Timestamp.valueOf(date_Fim.getValue().atTime(23, 59, 59));

            }

        }

        ReportExporter.exporterCSV(loadedIntervals, selectedItens, local, data_inicio, data_fim); // TODO: Use filtered Intervals
    }
}
