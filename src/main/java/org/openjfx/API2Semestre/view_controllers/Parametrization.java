package org.openjfx.api2semestre.view.controllers.views;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.report.IntervalFee;
import org.openjfx.api2semestre.view_macros.TextFieldTimeFormat;
import org.openjfx.api2semestre.view_utils.Expedient;
import org.openjfx.api2semestre.view_utils.IntervalFeeWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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

        tf_closingDay.setText(Expedient.getClosingDay().toString());
        tf_inicio.setText(Expedient.getNightShiftStart().format(DateTimeFormatter.ofPattern("HH:mm")));
        TextFieldTimeFormat.addTimeVerifier(tf_inicio);
        tf_fim.setText(Expedient.getNightShiftEnd().format(DateTimeFormatter.ofPattern("HH:mm")));
        TextFieldTimeFormat.addTimeVerifier(tf_fim);
        
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

        col_codigo.setCellValueFactory( new PropertyValueFactory<>( "codigo" ));
        col_codigo.setCellFactory(col -> new TableCell<IntervalFeeWrapper, Integer>() {
            private TextField textField;
        
            @Override
            public void startEdit() {
                super.startEdit();
        
                if (textField == null) {
                    textField = new TextField(getItem().toString());
                    textField.setOnAction(event -> commitEdit(Integer.parseInt(textField.getText())));
                }
        
                setGraphic(textField);
                setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                textField.requestFocus();
                textField.selectAll();
            }
        
            @Override
            public void cancelEdit() {
                super.cancelEdit();
                setText(getItem().toString());
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
        
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.toString());
                }
            }
        
            @Override
            public void commitEdit(Integer newValue) {
                if (!newValue.equals(getItem())) {
                    getTableRow().getItem().getIntervalFee().setCode(newValue);
                }
        
                super.commitEdit(newValue);
                setContentDisplay(ContentDisplay.TEXT_ONLY);
            }
        });
        
        col_tipo.setCellValueFactory( new PropertyValueFactory<>( "tipo" ));
        col_expediente.setCellValueFactory( new PropertyValueFactory<>( "expediente" ));
        col_fimDeSemana.setCellValueFactory( new PropertyValueFactory<>( "fimDeSemana" ));
        col_horaMinimo.setCellValueFactory( new PropertyValueFactory<>( "horaMinimo" ));
        col_horaDuracao.setCellValueFactory( new PropertyValueFactory<>( "horaDuracao" ));
        col_porcentagem.setCellValueFactory( new PropertyValueFactory<>( "porcentagem" ));

        updateTable();
    }
        
    
    private void updateTable() {
        loadedIntervalFees = Arrays.asList(IntervalFee.VERBAS);

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
    }
}
