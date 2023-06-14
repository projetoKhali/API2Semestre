package org.openjfx.api2semestre.view.controllers.views;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
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

    @FXML private DatePicker dp_inicio = new DatePicker(null);
    @FXML private DatePicker dp_fim = new DatePicker(null);
    
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

    private LinkedList<ReportIntervalWrapper> intervalsToExport = new LinkedList<>();
    private ReportInterval[] loadedIntervals;
    
    public void initialize() {
        Expedient.loadData();
        buildCheckBoxesPeriod();
        buildCheckBoxes();
        buildTable();
        updateTable();
    }

    private void choosePeriod() {
        if (cb_fechaAtual.isSelected() || cb_fechaAnterior.isSelected()) {
            dp_inicio.setDisable(true);
            dp_fim.setDisable(true);
        } else {
            dp_inicio.setDisable(false);
            dp_fim.setDisable(false);
        }
        applyFilter(false);
    }

    private void buildCheckBoxesPeriod() {
        dp_inicio.setDisable(true);
        dp_fim.setDisable(true);

        dp_inicio.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((oldValue == null) != (newValue == null) || oldValue != null && !oldValue.equals(newValue)) {
                applyFilter(false);
            }
        });
        dp_fim.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((oldValue == null) != (newValue == null) || oldValue != null && !oldValue.equals(newValue)) {
                applyFilter(false);
            }
        });

        cb_fechaAtual.setSelected(true);
        cb_fechaAnterior.setSelected(true);

        cb_fechaAtual.setOnAction(event -> choosePeriod());
        cb_fechaAnterior.setOnAction(event -> choosePeriod());
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

    @SuppressWarnings("unchecked") private void buildTable () {

        ChangeListener<Boolean> applyFilterCallback = new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                applyFilter(true);
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

        col_inicio.setCellValueFactory(new PropertyValueFactory<>("start"));
        col_fim.setCellValueFactory(new PropertyValueFactory<>("end"));
        col_total.setCellValueFactory(new PropertyValueFactory<>("total"));
        col_justificativa.setCellValueFactory(new PropertyValueFactory<>("justification"));
    }

    private void updateTable () {        
        loadedIntervals = AppointmentCalculator.calculateReports(QueryLibs.selectAllAppointments());
        System.out.println(loadedIntervals.length + " intervals");
        applyFilter(true);
    }

    private void applyFilter (boolean includeTextFieldFilter) {
        // System.out.println("Report.applyFilter -- " + intervalsToExport.size() + " intervals at start of call");

        // Inicia a conexão com o banco de dados
        Optional<java.sql.Connection> connectionOptional = QueryLibs.connect();

        // filtro de escrever
        if (includeTextFieldFilter) intervalsToExport = IntervalFilter.filterFromView(
            new LinkedList<ReportIntervalWrapper>(Arrays.asList(loadedIntervals).stream().map(interval -> {
                return new ReportIntervalWrapper(
                    QueryLibs.selectAppointmentById(
                        interval.getAppointmmentId(),
                        connectionOptional
                    ).get(),
                    interval
                );
            }).collect(Collectors.toList())),
            col_matricula_enableFilter.get() ? Optional.of(col_matricula) : Optional.empty(),
            col_colaborador_enableFilter.get() ? Optional.of(col_colaborador) : Optional.empty(),
            col_verba_enableFilter.get() ? Optional.of(col_verba) : Optional.empty(),
            col_cr_enableFilter.get() ? Optional.of(col_cr) : Optional.empty(),
            col_cliente_enableFilter.get() ? Optional.of(col_cliente) : Optional.empty(),
            col_projeto_enableFilter.get() ? Optional.of(col_projeto) : Optional.empty()
        );
        else intervalsToExport = new LinkedList<ReportIntervalWrapper>(Arrays.asList(loadedIntervals).stream().map(interval -> {
            return new ReportIntervalWrapper(
                QueryLibs.selectAppointmentById(
                    interval.getAppointmmentId(),
                    connectionOptional
                ).get(),
                interval
            );
        }).collect(Collectors.toList()));

        // Fecha a conexão com o banco de dados
        QueryLibs.close(connectionOptional);

        // System.out.println("Report.applyFilter -- " + intervalsToExport.size() + " intervals after match filter");

        Integer dia_inicio;
        Integer dia_fim;
        ObjectProperty<Timestamp> data_inicio = new SimpleObjectProperty<>();
        ObjectProperty<Timestamp> data_fim = new SimpleObjectProperty<>();

        LocalDateTime hoje = LocalDate.now().atTime(0, 0, 0);
        Integer mesAtual = hoje.getMonthValue();
        int anoAtual = hoje.getYear();

        data_inicio.set(Timestamp.valueOf((hoje.plusDays(1)).minusMonths(6)));
        data_fim.set(Timestamp.valueOf(hoje.plusDays(1)));
        
        if (cb_fechaAtual.isSelected() & cb_fechaAnterior.isSelected()) {
            dia_fim = Expedient.getClosingDay();
            dia_inicio = dia_fim + 1;
            if (hoje.getDayOfMonth() >= dia_inicio) {
                data_inicio.set(Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(1)));
            }
            else {
                data_inicio.set(Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(2)));
            }
            data_fim.set(Timestamp.valueOf(hoje.plusDays(1)));
            
        }
        else if (cb_fechaAtual.isSelected()) {
            dia_fim = Expedient.getClosingDay();
            dia_inicio = dia_fim + 1;
            if (hoje.getDayOfMonth() >= dia_inicio) {
                data_inicio.set(Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay())));
            }
            else {
                data_inicio.set(Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(1)));
            }
            data_fim.set(Timestamp.valueOf(hoje.plusDays(1)));
            
        }
        else if (cb_fechaAnterior.isSelected()) {
            dia_fim = Expedient.getClosingDay();
            dia_inicio = dia_fim + 1;
            if (hoje.getDayOfMonth() >= dia_inicio) {
                if (hoje.getDayOfMonth()!= 1) {
                    data_inicio.set(Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(1)));
                    data_fim.set(Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_fim + 1).atTime(0, 0, 0))));
                }
                else {
                    data_inicio.set(Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(1)));
                    data_fim.set(Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_fim + 1).atTime(0, 0, 0)).minusMonths(1)));
                }
            }
            else {
                data_inicio.set(Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_inicio).atStartOfDay()).minusMonths(2)));
                data_fim.set(Timestamp.valueOf((LocalDate.of(anoAtual, mesAtual, dia_fim + 1).atTime(0, 0, 0)).minusMonths(1)));
            }
        }

        else {
            if (dp_inicio.getValue() != null && dp_fim.getValue() != null) {
                data_inicio.set(Timestamp.valueOf(dp_inicio.getValue().atStartOfDay()));
                data_fim.set(Timestamp.valueOf(dp_fim.getValue().plusDays(1).atTime(0, 0, 0)));

            }
        }

        // System.out.println("data_inicio == null ? " +( data_inicio == null) + " | " + (data_inicio == null ? "-" : data_inicio.get()));
        // System.out.println("data_fim == null ? " + (data_fim == null) + " | " + (data_fim == null ? "-" : data_fim.get()));

        intervalsToExport = new LinkedList<ReportIntervalWrapper>(intervalsToExport.stream().filter(
            (ReportIntervalWrapper reportInterval) -> !(
                reportInterval.getInterval().getStart().after(data_fim.get())
                || 
                reportInterval.getInterval().getEnd().before(data_inicio.get())
            )
        ).collect(Collectors.toList()));

        // System.out.println("Report.applyFilter -- " + intervalsToExport.size() + " intervals after period filter");

        tabela.setItems(FXCollections.observableArrayList(intervalsToExport));
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

        // Inicia a conexão com o banco de dados
        Optional<java.sql.Connection> connectionOptional = QueryLibs.connect();

        ReportExporter.exportCSV(
            intervalsToExport.stream().map(riw -> riw.getInterval()).collect(Collectors.toList()), 
            selectedItens, 
            local,
            connectionOptional
        );

        // Fecha a conexão com o banco de dados
        QueryLibs.close(connectionOptional);

    }
}
