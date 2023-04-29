package org.openjfx.api2semestre.view_controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.appointments.Appointment;
import org.openjfx.api2semestre.appointments.AppointmentType;
import org.openjfx.api2semestre.appointments.Status;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.custom_tags.ViewConfig;
import org.openjfx.api2semestre.data_utils.DateConverter;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view_controllers.popups.PopUpFeedbackController;
import org.openjfx.api2semestre.view_macros.ColumnConfig;
import org.openjfx.api2semestre.view_macros.ColumnConfigStatus;
import org.openjfx.api2semestre.view_macros.ColumnConfigString;
import org.openjfx.api2semestre.view_macros.TableMacros;
import org.openjfx.api2semestre.view_utils.AppointmentFilter;
import org.openjfx.api2semestre.view_utils.AppointmentWrapper;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    // private BooleanProperty col_status_enableFilter = new SimpleBooleanProperty();

    @FXML
    private TableColumn<AppointmentWrapper, String> col_squad;
    private BooleanProperty col_squad_enableFilter = new SimpleBooleanProperty();

    @FXML
    private TableColumn<AppointmentWrapper, String> col_tipo;
    private BooleanProperty col_tipo_enableFilter = new SimpleBooleanProperty();

    @FXML
    private TableColumn<AppointmentWrapper, String> col_inicio;
    private BooleanProperty col_inicio_enableFilter = new SimpleBooleanProperty();

    @FXML
    private TableColumn<AppointmentWrapper, String> col_fim;
    private BooleanProperty col_fim_enableFilter = new SimpleBooleanProperty();

    @FXML
    private TableColumn<AppointmentWrapper, String> col_cliente;
    private BooleanProperty col_cliente_enableFilter = new SimpleBooleanProperty();

    @FXML
    private TableColumn<AppointmentWrapper, String> col_projeto;
    private BooleanProperty col_projeto_enableFilter = new SimpleBooleanProperty();

    @FXML
    private TableColumn<AppointmentWrapper, String> col_total;

    @FXML 
    private TableView<AppointmentWrapper> tabela;
    private ObservableList<AppointmentWrapper> displayedAppointments;
    private List<Appointment> loadedAppointments;
    

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        buildTable();

        updateTable();
    }

    @SuppressWarnings("unchecked")
    private void buildTable () {

        ChangeListener<Boolean> applyFilterCallback = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                applyFilter();
            }
        };

        TableMacros.buildTable(
            tabela,
            new ColumnConfig[] {
                new ColumnConfigStatus(col_status, "status", "Status"),
                new ColumnConfigString<>(col_squad, "squad", "CR", Optional.of(col_squad_enableFilter)),
                new ColumnConfigString<>(col_tipo, "type", "Tipo", Optional.of(col_tipo_enableFilter)),
                new ColumnConfigString<>(col_inicio, "startDate", "Data Início", Optional.of(col_inicio_enableFilter)),
                new ColumnConfigString<>(col_fim, "endDate", "Data Fim", Optional.of(col_fim_enableFilter)),
                new ColumnConfigString<>(col_cliente, "client", "Cliente", Optional.of(col_cliente_enableFilter)),
                new ColumnConfigString<>(col_projeto, "project", "Projeto", Optional.of(col_projeto_enableFilter)),
                new ColumnConfigString<>(col_total, "total", "Total", Optional.empty())
            },
            Optional.of(applyFilterCallback)
        );

        tabela.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() != 1) return;

                Status targetStatus = Status.Rejected; // TODO: change to Rejected in production
                AppointmentWrapper selectedItem = tabela.getSelectionModel().getSelectedItem();
                if (selectedItem == null || selectedItem.getAppointment().getStatus() != targetStatus) return;

                // System.out.println(selectedItem.toString());
                PopUpFeedbackController.apt_selected = selectedItem;
                popUp("popups/popUpFeedback");
                                    
            }
        });
    }

    private void updateTable () {
    
        loadedAppointments = Arrays.asList(QueryLibs.collaboratorSelect(Authentication.getCurrentUser().getNome()));
        System.out.println(loadedAppointments.size() + " appointments returned from select ");

        applyFilter();
    }

    private void applyFilter () {

        // System.out.println("applyFilter");

        List<Appointment> appointmentsToDisplay = AppointmentFilter.filterFromView(
            loadedAppointments,
            Optional.empty(),
            col_tipo_enableFilter.get() ? Optional.of(col_tipo) : Optional.empty(),
            col_inicio_enableFilter.get() ? Optional.of(col_inicio) : Optional.empty(),
            col_fim_enableFilter.get() ? Optional.of(col_fim) : Optional.empty(),
            col_squad_enableFilter.get() ? Optional.of(col_squad) : Optional.empty(),
            col_cliente_enableFilter.get() ? Optional.of(col_cliente) : Optional.empty(),
            col_projeto_enableFilter.get() ? Optional.of(col_projeto) : Optional.empty(),
            Optional.of(Status.Pending)
        );

        displayedAppointments = FXCollections.observableArrayList(
            appointmentsToDisplay.stream().map((Appointment apt) -> new AppointmentWrapper(apt)).collect(Collectors.toList())
        );

        tabela.setItems(displayedAppointments);
        tabela.refresh();

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

        displayedAppointments.add(new AppointmentWrapper(appointment));


        // // Testes de Permissions na tag ViewConfig:
        // ObservableList<Permission> permissions = view.getPermissions();
        // System.out.println(permissions.size() + " Permissions");
        // for (Permission p: permissions) {
        //     System.out.println("Permission: " + p.getValue());
        // }



    }
        
    // função usada para exibir um pop up, que deve corresponder ao fxml de nome fileName
    void popUp(String fileName){
        try{
        
            Stage stage;
            Parent root;
           
            stage = new Stage();
    
            root = FXMLLoader.load(PopUpFeedbackController.class.getResource(fileName));
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(tabela.getScene().getWindow());
            stage.showAndWait();
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
        
    
         

    

}