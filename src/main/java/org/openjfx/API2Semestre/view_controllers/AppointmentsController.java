package org.openjfx.api2semestre.view_controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.classes.Appointment;
import org.openjfx.api2semestre.classes.AppointmentType;

import org.openjfx.api2semestre.custom_tags.ViewConfig;
import org.openjfx.api2semestre.data_utils.DateConverter;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view_utils.AppointmentWrapper;
import org.openjfx.api2semestre.view_utils.PrettyTableCell;
import org.openjfx.api2semestre.view_utils.PrettyTableCellInstruction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

public class AppointmentsController implements Initializable {

    @FXML
    private ViewConfig view;

    @FXML
    private TextField cx_cliente;

    @FXML
    private DatePicker cx_dataFinal;

    @FXML
    private DatePicker cx_dataInicio;

    @FXML
    private TextField cx_horaFinal;

    @FXML
    private TextField cx_horaInicio;

    @FXML
    private TextField cx_justificativa;

    @FXML
    private TextField cx_projeto;

    @FXML
    private TextField cx_squad;

    @FXML
    private Button bt_horaExtra;

    @FXML
    private Button bt_sobreaviso;

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
    private TableView<AppointmentWrapper> tabela;
    private ObservableList<AppointmentWrapper> loadedAppointments;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        buildTable();

        updateTable();
    }

    private void buildTable () {
        col_status.setCellValueFactory( new PropertyValueFactory<>( "status" ));
        col_status.setCellFactory(column -> {
            List<PrettyTableCellInstruction<AppointmentWrapper, String>> instructions = new ArrayList<>();
            instructions.add(new PrettyTableCellInstruction<>(Optional.of("Pendente"), new Color(0.97, 1, 0.6, 1)));
            instructions.add(new PrettyTableCellInstruction<>(Optional.of("Aprovado"), new Color(0.43, 0.84, 0.47, 1)));
            instructions.add(new PrettyTableCellInstruction<>(Optional.of("Rejeitado"), new Color(0.87, 0.43, 0.43, 1)));
            
            // return new PrettyTableCell<>(column, instructions.toArray(new PrettyTableCellInstruction[0]));
            return new PrettyTableCell<>(instructions.toArray(new PrettyTableCellInstruction[0]));
        });
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

    @FXML
    void inputHoraExtra(ActionEvent event){
        inputAppointment(AppointmentType.Overtime);
    }

    @FXML
    void inputSobreaviso(ActionEvent event) {
        inputAppointment(AppointmentType.OnNotice);
    }

    void inputAppointment (AppointmentType type) {
        Appointment appointment = new Appointment(
            "Fulano",
            type,
            DateConverter.inputToTimestamp(cx_dataInicio.getValue(),cx_horaInicio.getText()),
            DateConverter.inputToTimestamp(cx_dataFinal.getValue(),cx_horaFinal.getText()),
            cx_squad.getText(),
            cx_cliente.getText(),
            cx_projeto.getText(),
            cx_justificativa.getText()
        );
        System.out.println("New Appointment -- startDate: " + appointment.getStartDate() + " | endDate: " + appointment.getEndDate());
        QueryLibs.insertTable(appointment);

        loadedAppointments.add(new AppointmentWrapper(appointment));


        // // Testes de Permissions na tag ViewConfig:
        // ObservableList<Permission> permissions = view.getPermissions();
        // System.out.println(permissions.size() + " Permissions");
        // for (Permission p: permissions) {
        //     System.out.println("Permission: " + p.getValue());
        // }



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