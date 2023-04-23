package org.openjfx.api2semestre.view_controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.classes.Appointment;
import org.openjfx.api2semestre.custom_tags.ViewConfig;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view_macros.TableCheckBoxMacros;
import org.openjfx.api2semestre.view_utils.AppointmentWrapper;
import org.openjfx.api2semestre.view_utils.PrettyTableCell;
import org.openjfx.api2semestre.view_utils.PrettyTableCellInstruction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class ApprovalsController implements Initializable {

    @FXML
    private ViewConfig view;

    @FXML
    private Button btn_approve;

    @FXML
    private Button btn_reject;

    @FXML
    private TableColumn<AppointmentWrapper, Boolean> col_selecionar;

    @FXML
    private TableColumn<AppointmentWrapper, String> col_requester;

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
    private TableView<AppointmentWrapper> tabela;
    private ObservableList<AppointmentWrapper> loadedAppointments;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
    
        buildTable();

        updateTable();

    }

    private void buildTable () {
        col_selecionar.setCellValueFactory( new PropertyValueFactory<>( "selected" ));
        TableCheckBoxMacros.setCheckBoxHeader(tabela, col_selecionar);
        col_requester.setCellValueFactory( new PropertyValueFactory<>( "requester" ));
        col_squad.setCellValueFactory( new PropertyValueFactory<>( "squad" ));
        col_tipo.setCellValueFactory( new PropertyValueFactory<>( "type" ));
        col_inicio.setCellValueFactory( new PropertyValueFactory<>( "startDate" ));
        col_fim.setCellValueFactory( new PropertyValueFactory<>( "endDate" ));
        col_cliente.setCellValueFactory( new PropertyValueFactory<>( "client" ));
        col_projeto.setCellValueFactory( new PropertyValueFactory<>( "project" ));
        col_total.setCellValueFactory( new PropertyValueFactory<>( "total" ));
    }

    private void updateTable () {
        Appointment[] items = QueryLibs.collaboratorSelect("Fulano");
        System.out.println(items.length + " appointments returned from select ");
    
        loadedAppointments = FXCollections.observableArrayList(
            Arrays.asList(items).stream().map((Appointment apt) -> new AppointmentWrapper(apt)).collect(Collectors.toList())
        );

        tabela.setItems(loadedAppointments);
    }

    
//     @FXML
//     void showPopUp(ActionEvent event) throws IOException {
//           popUp("popUpFeedback.fxml", bt_testePopUp);
    
//     }
    
//     // função usada para exibir um pop up, que deve corresponder ao fxml de nome fileName
//     void popUp(String fileName, Button botao) throws IOException{
        
//         Stage stage;
//         Parent root;
// //        if(event.getSource()==bt_testePopUp){
//         stage = new Stage();
//         root = FXMLLoader.load(getClass().getResource(fileName));
//         stage.setScene(new Scene(root));
//         stage.initModality(Modality.APPLICATION_MODAL);
//         stage.initOwner(botao.getScene().getWindow());
//         stage.showAndWait();
    
//     }
        
         


}