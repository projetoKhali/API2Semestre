package org.openjfx.api2semestre.view_controllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.openjfx.api2semestre.classes.Appointment;
import org.openjfx.api2semestre.classes.AppointmentType;

import org.openjfx.api2semestre.custom_tags.Permission;
import org.openjfx.api2semestre.custom_tags.ViewConfig;

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

// import org.openjfx.API2Semestre.Classes.Appointment;
// import org.openjfx.API2Semestre.Classes.AppointmentType;
// import org.openjfx.API2Semestre.JavaFiles.QueryLibs;

public class PrimaryController implements Initializable {

    @FXML
    private ViewConfig view;

    @FXML
    private Button bt_horaExtra;

    @FXML
    private Button bt_sobreaviso;

    @FXML
    private TableColumn<Appointment, String> col_extraOUsobreaviso;

    @FXML
    private TableColumn<Appointment, String> col_feedback;

    @FXML
    private TableColumn<Appointment, String> col_periodo;

    @FXML
    private TableColumn<Appointment, String> col_squad;

    @FXML
    private TableColumn<Appointment, String> col_status;

    @FXML
    private TableColumn<Appointment, String> col_total;

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
    private TableView<Appointment> tabela;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        col_squad.setCellValueFactory(new PropertyValueFactory<>("squad"));

        // System.out.println("oi " + view);
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
        // Appointment appointment = new Appointment(
        //     "Fulano",
        //     type,
        //     DateConverter.inputToTimestamp(cx_dataInicio.getValue(),cx_horaInicio.getText()),
        //     DateConverter.inputToTimestamp(cx_dataFinal.getValue(),cx_horaFinal.getText()),
        //     cx_squad.getText(),
        //     cx_cliente.getText(),
        //     cx_projeto.getText(),
        //     cx_justificativa.getText()
        // );
        // System.out.println("New Appointment -- startDate: " + appointment.getStartDate() + " | endDate: " + appointment.getEndDate());
        // QueryLibs.insertTable(null, appointment);

        // System.out.println("oi " + view);

        ObservableList<Permission> permissions = view.getPermissions();
        System.out.println(permissions.size() + " Permissions");

        for (Permission p: permissions) {
            System.out.println("Permission: " + p.getValue());
        }

    }

}