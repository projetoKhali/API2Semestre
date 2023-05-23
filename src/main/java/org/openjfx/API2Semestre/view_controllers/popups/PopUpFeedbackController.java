package org.openjfx.api2semestre.view.controllers.views.popups;

import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import org.openjfx.api2semestre.view_utils.AppointmentWrapper;
import org.openjfx.api2semestre.view_utils.PrettyTableCell;
import org.openjfx.api2semestre.view_utils.PrettyTableCellInstruction;


public class PopUpFeedbackController {

    @FXML private TableView<AppointmentWrapper> tabela;
    
    @FXML private TableColumn<AppointmentWrapper, String> col_status;
    @FXML private TableColumn<AppointmentWrapper, String> col_squad;
    @FXML private TableColumn<AppointmentWrapper, String> col_tipo;
    @FXML private TableColumn<AppointmentWrapper, String> col_inicio;
    @FXML private TableColumn<AppointmentWrapper, String> col_fim;
    @FXML private TableColumn<AppointmentWrapper, String> col_cliente;
    @FXML private TableColumn<AppointmentWrapper, String> col_projeto;
    @FXML private TableColumn<AppointmentWrapper, String> col_total;
   
    @FXML private Label lb_feedback;

    
    public static AppointmentWrapper apt_selected;
    
    
    public void initialize(){
        // System.out.println("testeoi");
        lb_feedback.setText(apt_selected.getFeedback());
        buildTable();
    }
    
    @SuppressWarnings("unchecked")
    private void buildTable () {
        col_status.setCellValueFactory( new PropertyValueFactory<>( "status" ));
        col_status.setCellFactory(column -> {
            return new PrettyTableCell<AppointmentWrapper, String>(new PrettyTableCellInstruction[] {
                new PrettyTableCellInstruction<AppointmentWrapper, String>(Optional.of("Pendente"), new Color(0.97, 1, 0.6, 1)),
                new PrettyTableCellInstruction<AppointmentWrapper, String>(Optional.of("Aprovado"), new Color(0.43, 0.84, 0.47, 1)),
                new PrettyTableCellInstruction<AppointmentWrapper, String>(Optional.of("Rejeitado"), new Color(0.87, 0.43, 0.43, 1))
            });
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