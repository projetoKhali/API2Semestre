package org.openjfx.api2semestre.view_controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.report.IntervalFee;
import org.openjfx.api2semestre.report.Week;
import org.openjfx.api2semestre.view_utils.IntervalFeeWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class Parametrization {
 
    @FXML private TableView<IntervalFeeWrapper> tabela;
    @FXML private TableColumn<IntervalFeeWrapper, Integer> col_verba;
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
        loadedIntervalFees = List.of(
            new IntervalFee(1000, 1.00f, Week.ALL.get(), 0, 0, 0, false),
            new IntervalFee(1001, 1.25f, Week.FDS.get(), 0, 0, 0, false),
            new IntervalFee(1002, 1.47f, Week.ALL.get(), 22, 6, 0, true),
            new IntervalFee(1002, 2.00f, Week.ALL.get(), 0, 0, 2, true)
        );

        displayedIntervalFees = FXCollections.observableArrayList(
            loadedIntervalFees.stream().map((intervalFee) -> new IntervalFeeWrapper(intervalFee)).collect(Collectors.toList())
        );

        tabela.setItems(displayedIntervalFees);
        tabela.refresh();

    }
}
