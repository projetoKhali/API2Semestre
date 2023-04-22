package org.openjfx.api2semestre.view_controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.classes.Appointment;
import org.openjfx.api2semestre.classes.AppointmentType;

import org.openjfx.api2semestre.custom_tags.ViewConfig;
import org.openjfx.api2semestre.data_utils.DateConverter;
import org.openjfx.api2semestre.view_utils.AppointmentWrapper;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TableView<AppointmentWrapper> tabela;
    
 
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        // col_squad.setCellValueFactory(new PropertyValueFactory<>("squad"));

        // System.out.println("oi " + view);

        final List<Appointment> items = List.of(
            new Appointment(
                "teste1",
                AppointmentType.Overtime,
                DateConverter.inputToTimestamp(LocalDate.of(2013, 12, 1), "11:00"),
                DateConverter.inputToTimestamp(LocalDate.of(2013, 12, 1), "12:00"),
                "khali",
                "2rp",
                "api2sem",
                "oi"
            ),
            new Appointment(
                "teste2",
                AppointmentType.OnNotice,
                DateConverter.inputToTimestamp(LocalDate.of(2013, 12, 1), "11:00"),
                DateConverter.inputToTimestamp(LocalDate.of(2013, 12, 1), "12:00"),
                "khali",
                "2rp",
                "api2sem",
                "olá"
            ),
            new Appointment(
                "teste3",
                AppointmentType.Overtime,
                DateConverter.inputToTimestamp(LocalDate.of(2013, 12, 1), "11:00"),
                DateConverter.inputToTimestamp(LocalDate.of(2013, 12, 1), "12:00"),
                "khali",
                "2rp",
                "api2sem",
                "eai"
            )
        );

        
        final var columns = tabela.getColumns();

        columns.get(0).setCellValueFactory( new PropertyValueFactory<>( "selected" ));
        columns.get(0).setCellFactory( tc -> new CheckBoxTableCell<>());


        columns.get(0 + 1).setCellValueFactory( new PropertyValueFactory<>( "status" ));
        columns.get(1 + 1).setCellValueFactory( new PropertyValueFactory<>( "squad" ));
        columns.get(2 + 1).setCellValueFactory( new PropertyValueFactory<>( "type" ));
        columns.get(3 + 1).setCellValueFactory( new PropertyValueFactory<>( "startDate" ));
        columns.get(4 + 1).setCellValueFactory( new PropertyValueFactory<>( "endDate" ));
        columns.get(5 + 1).setCellValueFactory( new PropertyValueFactory<>( "client" ));
        columns.get(6 + 1).setCellValueFactory( new PropertyValueFactory<>( "project" ));
        // columns.get(0).setCellValueFactory( new PropertyValueFactory<>( "justification" ));

        tabela.setItems(
            FXCollections.observableArrayList(
                items.stream().map((Appointment apt) -> new AppointmentWrapper(apt)).collect(Collectors.toList())
            )
        );

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

        // ObservableList<Permission> permissions = view.getPermissions();
        // System.out.println(permissions.size() + " Permissions");

        // for (Permission p: permissions) {
        //     System.out.println("Permission: " + p.getValue());
        // }



    }

}