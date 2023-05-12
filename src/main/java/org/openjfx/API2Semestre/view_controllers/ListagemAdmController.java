package org.openjfx.api2semestre.view_controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.appointments.Appointment;
import org.openjfx.api2semestre.custom_tags.ViewConfig;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view_macros.ColumnConfig;
import org.openjfx.api2semestre.view_macros.ColumnConfigString;
import org.openjfx.api2semestre.view_macros.TableCheckBoxMacros;
import org.openjfx.api2semestre.view_macros.TableMacros;
import org.openjfx.api2semestre.view_utils.AppointmentFilter;
import org.openjfx.api2semestre.view_utils.AppointmentWrapper;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ListagemAdmController implements Initializable {

    @FXML
    private ViewConfig view;


    @FXML
    private TableColumn<AppointmentWrapper, Boolean> col_selecionar;

    @FXML
    private TableColumn<AppointmentWrapper, String> col_requester;
    private BooleanProperty col_requester_enableFilter = new SimpleBooleanProperty();

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

        col_selecionar.setCellValueFactory( new PropertyValueFactory<>( "selected" ));
        TableCheckBoxMacros.setCheckBoxHeader(tabela, col_selecionar);

        TableMacros.buildTable(
            tabela,
            new ColumnConfig[] {
                new ColumnConfigString<>(col_requester, "requester", "Solicitante", Optional.of(col_requester_enableFilter)),
                new ColumnConfigString<>(col_squad, "squad", "CR", Optional.of(col_squad_enableFilter)),
                new ColumnConfigString<>(col_tipo, "type", "Tipo", Optional.empty()),
                new ColumnConfigString<>(col_inicio, "startDate", "Data Início", Optional.empty()),
                new ColumnConfigString<>(col_fim, "endDate", "Data Fim", Optional.empty()),
                new ColumnConfigString<>(col_cliente, "client", "Cliente", Optional.of(col_cliente_enableFilter)),
                new ColumnConfigString<>(col_projeto, "project", "Projeto", Optional.of(col_projeto_enableFilter)),
                new ColumnConfigString<>(col_total, "total", "Total", Optional.empty())
            },
            Optional.of(applyFilterCallback)
        );
    }

    private void updateTable () {
        Appointment[] items = QueryLibs.selectAllAppointments();
        System.out.println(items.length + " appointments returned from select ");
    
        loadedAppointments = Arrays.asList(items);

        displayedAppointments = FXCollections.observableArrayList(
            loadedAppointments.stream().map((Appointment apt) -> new AppointmentWrapper(apt)).collect(Collectors.toList())
        );

        tabela.setItems(displayedAppointments);
    }

    private void applyFilter () {

        System.out.println("applyFilter");

        List<Appointment> appointmentsToDisplay = AppointmentFilter.filterFromView(
            loadedAppointments,
            col_requester_enableFilter.get() ? Optional.of(col_requester) : Optional.empty(),
            col_tipo_enableFilter.get() ? Optional.of(col_tipo) : Optional.empty(),
            col_inicio_enableFilter.get() ? Optional.of(col_inicio) : Optional.empty(),
            col_fim_enableFilter.get() ? Optional.of(col_fim) : Optional.empty(),
            col_squad_enableFilter.get() ? Optional.of(col_squad) : Optional.empty(),
            col_cliente_enableFilter.get() ? Optional.of(col_cliente) : Optional.empty(),
            col_projeto_enableFilter.get() ? Optional.of(col_projeto) : Optional.empty(),
            Optional.empty()
        );

        displayedAppointments = FXCollections.observableArrayList(
            appointmentsToDisplay.stream().map((Appointment apt) -> new AppointmentWrapper(apt)).collect(Collectors.toList())
        );

        tabela.setItems(displayedAppointments);
        tabela.refresh();

        col_selecionar.setGraphic(null);
        TableCheckBoxMacros.setCheckBoxHeader(tabela, col_selecionar);
    }

    
//     @FXML
//     void showPopUp(ActionEvent event) throws IOException {
//           popUp("popups/popUpFeedback", bt_testePopUp);
    
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