package org.openjfx.API2Semestre;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.openjfx.API2Semestre.Classes.Appointment;
import org.openjfx.API2Semestre.Classes.AppointmentType;
import org.openjfx.API2Semestre.JavaFiles.QueryLibs;

import static org.openjfx.API2Semestre.Classes.AppointmentType.OnNotice;
import static org.openjfx.API2Semestre.Classes.AppointmentType.Overtime;

public class PrimaryController {

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
    
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle){
        col_squad.setCellValueFactory(new PropertyValueFactory<>("squad"));
    }

    @FXML
    void inputHoraExtra(ActionEvent event){
        inputAppointment(Overtime);
    }

    @FXML
    void inputSobreaviso(ActionEvent event) {
        inputAppointment(OnNotice);
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
        // QueryLibs.insertTable(null, appointment);
    }

}