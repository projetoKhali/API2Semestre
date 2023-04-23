package org.openjfx.api2semestre.view_controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.openjfx.api2semestre.view_utils.AppointmentWrapper;
import org.openjfx.api2semestre.view_utils.PrettyTableCell;
import org.openjfx.api2semestre.view_utils.PrettyTableCellInstruction;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class RejectPopupListItem implements PopupController {

    @FXML
    private TableView<AppointmentWrapper> tabela;

    @FXML
    private TableColumn<AppointmentWrapper, String> col_status;
    
    @FXML
    private TableColumn<AppointmentWrapper, String> col_squad;
    
    @FXML
    private TableColumn<AppointmentWrapper, String> col_tipo;
    
    @FXML
    private TableColumn<AppointmentWrapper, String> col_inicio;
    
    @FXML
    private TableColumn<AppointmentWrapper, String> col_fim;
    
    @FXML
    private TableColumn<AppointmentWrapper, String> col_cliente;
    
    @FXML
    private TableColumn<AppointmentWrapper, String> col_projeto;
    
    @FXML
    private TableColumn<AppointmentWrapper, String> col_total;
   
    @FXML
    private TextField textField;

    private AppointmentWrapper apt_selected;

    @Override
    public AppointmentWrapper getSelected() {
        return apt_selected;
    }

    @Override
    public void setSelected(AppointmentWrapper aptWrapper) {
        apt_selected = aptWrapper;
    }

    public void initialize(){

    }

    public String getFeedback () {
        return textField.getText();
    }
    
    public void buildTable () {
        // col_selecionar.setCellValueFactory( new PropertyValueFactory<>( "selected" ));
        // TableCheckBoxMacros.setCheckBoxHeader(tabela, col_selecionar);
        col_status.setCellValueFactory( new PropertyValueFactory<>( "status" ));
        col_status.setCellFactory(column -> {
            List<PrettyTableCellInstruction<AppointmentWrapper, String>> instructions = new ArrayList<>();
            instructions.add(new PrettyTableCellInstruction<>(Optional.of("Pendente"), new Color(0.97, 1, 0.6, 1)));
            instructions.add(new PrettyTableCellInstruction<>(Optional.of("Aprovado"), new Color(0.43, 0.84, 0.47, 1)));
            instructions.add(new PrettyTableCellInstruction<>(Optional.of("Rejeitado"), new Color(0.87, 0.43, 0.43, 1)));
            
            return new PrettyTableCell<>(instructions.toArray(new PrettyTableCellInstruction[0]));
        });
        col_squad.setCellValueFactory( new PropertyValueFactory<>( "squad" ));
        col_tipo.setCellValueFactory( new PropertyValueFactory<>( "type" ));
        col_inicio.setCellValueFactory( new PropertyValueFactory<>( "startDate" ));
        col_fim.setCellValueFactory( new PropertyValueFactory<>( "endDate" ));
        col_cliente.setCellValueFactory( new PropertyValueFactory<>( "client" ));
        col_projeto.setCellValueFactory( new PropertyValueFactory<>( "project" ));
        col_total.setCellValueFactory( new PropertyValueFactory<>( "total" ));
        // asdasdasdasdas( new PropertyValueFactory<>( "justification" ));
    
        tabela.setItems(FXCollections.observableArrayList(List.of(apt_selected)));
    }


}
