package org.openjfx.api2semestre.view.controllers.views;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.data.Expedient;
import org.openjfx.api2semestre.report.IntervalFee;
import org.openjfx.api2semestre.view.macros.TableMacros;
import org.openjfx.api2semestre.view.macros.TextFieldTimeFormat;
import org.openjfx.api2semestre.view.macros.TableMacros.Formatter;
import org.openjfx.api2semestre.view.utils.wrappers.IntervalFeeWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class Parametrization {

    @FXML private TextField tf_closingDay;
    @FXML private TextField tf_inicio;
    @FXML private TextField tf_fim;
 
    @FXML private TableView<IntervalFeeWrapper> tabela;
    @FXML private TableColumn<IntervalFeeWrapper, Integer> col_codigo;
    @FXML private TableColumn<IntervalFeeWrapper, String> col_tipo;
    @FXML private TableColumn<IntervalFeeWrapper, String> col_expediente;
    @FXML private TableColumn<IntervalFeeWrapper, String> col_fimDeSemana;
    @FXML private TableColumn<IntervalFeeWrapper, String> col_horaMinimo;
    @FXML private TableColumn<IntervalFeeWrapper, Double> col_horaDuracao;
    @FXML private TableColumn<IntervalFeeWrapper, Double> col_porcentagem;
    
    private ObservableList<IntervalFeeWrapper> displayedIntervalFees;
    private List<IntervalFee> loadedIntervalFees;

    public void initialize () {

        buildTable();

        updateTable();
    }

    private void buildTable () {

        Expedient.loadData();

        // Carrega os dados de Expedient para os campos de edição de dia de fechamento e inicio e fim do periodo noturno
        tf_closingDay.setText(Expedient.getClosingDay().toString());
        tf_inicio.setText(Expedient.getNightShiftStart().format(DateTimeFormatter.ofPattern("HH:mm")));
        tf_fim.setText(Expedient.getNightShiftEnd().format(DateTimeFormatter.ofPattern("HH:mm")));

        // Configura os campos de inicio e fim do periodo noturno para usarem validação do formato de tempo "HH:mm" na inserção
        TextFieldTimeFormat.addTimeVerifier(tf_inicio);
        TextFieldTimeFormat.addTimeVerifier(tf_fim);

        // Configura o campo de dia de fechamento para usar uma validação numérica entre 1 e 28
        tf_closingDay.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) tf_closingDay.setText(newValue.replaceAll("[^\\d]", ""));
            try {
                tf_closingDay.setText(Integer.toString(
                    Integer.max(Integer.min(Integer.valueOf(Integer.parseInt(tf_closingDay.getText())), 28), 1)
                ));
            } catch (NumberFormatException e) {
                tf_closingDay.setText("");
            }
        });

        col_codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        TableMacros.<IntervalFeeWrapper, Integer>enableEditableCells(
            col_codigo,
            (Integer value) -> value < 10000 && value >= 1000,
            (IntervalFeeWrapper item, Integer value) -> item.setCodigo(value),
            new Formatter<Integer>() {
                private final StringConverter<Integer> converter = new IntegerStringConverter();
                @Override public String format(Integer value, boolean editing) { return converter.toString(value); }
                @Override public String parse(String text) { return text; }
                @Override public StringConverter<Integer> getConverter() { return converter; }
            }
        );
        
        col_porcentagem.setCellValueFactory(new PropertyValueFactory<>("porcentagem"));
        TableMacros.<IntervalFeeWrapper, Double>enableEditableCells(
            col_porcentagem,
            (Double value) -> true,
            (IntervalFeeWrapper item, Double value) -> item.setPorcentagem(value),
            new Formatter<Double>() {
                private final StringConverter<Double> converter = new DoubleStringConverter();
                @Override public String format(Double value, boolean editing) { return converter.toString(value) + (editing ? "" : "%"); }
                @Override public String parse(String text) { return text.replaceAll("%", ""); }
                @Override public StringConverter<Double> getConverter() { return converter; }
            }
        );
        
        col_tipo.setCellValueFactory( new PropertyValueFactory<>("tipo"));
        col_expediente.setCellValueFactory( new PropertyValueFactory<>("expediente"));
        col_fimDeSemana.setCellValueFactory( new PropertyValueFactory<>("fimDeSemana"));
        col_horaMinimo.setCellValueFactory( new PropertyValueFactory<>("horaMinimo"));
        col_horaDuracao.setCellValueFactory( new PropertyValueFactory<>("horaDuracao"));

        updateTable();
    }
        
    
    private void updateTable() {
        loadedIntervalFees = Arrays.asList(IntervalFee.getVerbas());

        displayedIntervalFees = FXCollections.observableArrayList(
            loadedIntervalFees.stream().map((intervalFee) -> new IntervalFeeWrapper(intervalFee)).collect(Collectors.toList())
        );

        tabela.setItems(displayedIntervalFees);
        tabela.refresh();    
    }

    @FXML public void salvar (ActionEvent e) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        Expedient.saveData(
            LocalTime.parse(tf_inicio.getText(), formatter),
            LocalTime.parse(tf_fim.getText(), formatter),
            Integer.parseInt(tf_closingDay.getText())
        );
        IntervalFee.saveVerbas(
            displayedIntervalFees.stream().map(
                intervalFeeWrapper -> intervalFeeWrapper.getIntervalFee()
            ).collect(Collectors.toList()).toArray(IntervalFee[]::new)
        );
    }
}
