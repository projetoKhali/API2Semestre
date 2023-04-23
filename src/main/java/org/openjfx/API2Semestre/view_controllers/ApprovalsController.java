package org.openjfx.api2semestre.view_controllers;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.classes.Appointment;
import org.openjfx.api2semestre.custom_tags.ViewConfig;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view_macros.TableCheckBoxMacros;
import org.openjfx.api2semestre.view_macros.TableColumnFilterMacros;
import org.openjfx.api2semestre.view_utils.AppointmentFilter;
import org.openjfx.api2semestre.view_utils.AppointmentWrapper;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private void buildTable () {

        // tabela.setTableMenuButtonVisible(false);

        col_selecionar.setCellValueFactory( new PropertyValueFactory<>( "selected" ));
        col_selecionar.setReorderable(false);
        // col_selecionar.setResizable(false);
        TableCheckBoxMacros.setCheckBoxHeader(tabela, col_selecionar);

        col_requester.setCellValueFactory( new PropertyValueFactory<>( "requester" ));
        col_requester.setReorderable(false);
        // col_requester.setResizable(false);
        TableColumnFilterMacros.setTextFieldHeader(col_requester, "Solicitante", col_requester_enableFilter);

        col_squad.setCellValueFactory( new PropertyValueFactory<>( "squad" ));
        col_squad.setReorderable(false);
        // col_squad.setResizable(false);
        TableColumnFilterMacros.setTextFieldHeader(col_squad, "CR", col_squad_enableFilter);

        col_tipo.setCellValueFactory( new PropertyValueFactory<>( "type" ));
        col_tipo.setReorderable(false);
        // col_tipo.setResizable(false);
        TableColumnFilterMacros.setTextFieldHeader(col_tipo, "Tipo", col_tipo_enableFilter);

        col_inicio.setCellValueFactory( new PropertyValueFactory<>( "startDate" ));
        col_inicio.setReorderable(false);
        // col_inicio.setResizable(false);
        TableColumnFilterMacros.setTextFieldHeader(col_inicio, "Início", col_inicio_enableFilter);

        col_fim.setCellValueFactory( new PropertyValueFactory<>( "endDate" ));
        col_fim.setReorderable(false);
        // col_fim.setResizable(false);
        TableColumnFilterMacros.setTextFieldHeader(col_fim, "Fim", col_fim_enableFilter);

        col_cliente.setCellValueFactory( new PropertyValueFactory<>( "client" ));
        col_cliente.setReorderable(false);
        // col_cliente.setResizable(false);
        TableColumnFilterMacros.setTextFieldHeader(col_cliente, "Cliente", col_cliente_enableFilter);

        col_projeto.setCellValueFactory( new PropertyValueFactory<>( "project" ));
        col_projeto.setReorderable(false);
        // col_projeto.setResizable(false);
        TableColumnFilterMacros.setTextFieldHeader(col_projeto, "Projeto", col_projeto_enableFilter);

        col_total.setCellValueFactory( new PropertyValueFactory<>( "total" ));
        col_total.setReorderable(false);
        // col_total.setResizable(false);
    }

    private void updateTable () {
        Appointment[] items = QueryLibs.collaboratorSelect("Fulano");
        System.out.println(items.length + " appointments returned from select ");
    
        loadedAppointments = Arrays.asList(items);

        List<Appointment> appointmentsToDisplay = AppointmentFilter.filterFromView(
            loadedAppointments,
            col_requester_enableFilter.get() ? Optional.of(col_requester) : Optional.empty(),
            col_tipo_enableFilter.get() ? Optional.of(col_tipo) : Optional.empty(),
            col_inicio_enableFilter.get() ? Optional.of(col_inicio) : Optional.empty(),
            col_fim_enableFilter.get() ? Optional.of(col_fim) : Optional.empty(),
            col_squad_enableFilter.get() ? Optional.of(col_squad) : Optional.empty(),
            col_cliente_enableFilter.get() ? Optional.of(col_cliente) : Optional.empty(),
            col_projeto_enableFilter.get() ? Optional.of(col_projeto) : Optional.empty()
        );

        displayedAppointments = FXCollections.observableArrayList(
            appointmentsToDisplay.stream().map((Appointment apt) -> new AppointmentWrapper(apt)).collect(Collectors.toList())
        );

        tabela.setItems(displayedAppointments);
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