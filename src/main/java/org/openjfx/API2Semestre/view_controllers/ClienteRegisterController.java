package org.openjfx.api2semestre.view_controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.appointments.Appointment;
import org.openjfx.api2semestre.appointments.AppointmentType;
import org.openjfx.api2semestre.appointments.Status;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.custom_tags.ViewConfig;
import org.openjfx.api2semestre.data.Client;
import org.openjfx.api2semestre.data_utils.DateConverter;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view_controllers.popups.PopUpFeedbackController;
import org.openjfx.api2semestre.view_macros.ColumnConfig;
import org.openjfx.api2semestre.view_macros.ColumnConfigStatus;
import org.openjfx.api2semestre.view_macros.ColumnConfigString;
import org.openjfx.api2semestre.view_macros.TableMacros;
import org.openjfx.api2semestre.view_utils.AppointmentFilter;
import org.openjfx.api2semestre.view_utils.AppointmentWrapper;
import org.openjfx.api2semestre.view_utils.ClientFilter;
import org.openjfx.api2semestre.view_utils.ClientWrapper;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ClienteRegisterController implements Initializable {

    @FXML
    private TextField cx_razao;

    @FXML
    private Button bt_Comfirmar;

    @FXML
    private TableColumn<ClientWrapper, String> col_cnpj;
    private BooleanProperty col_cnpj_enableFilter = new SimpleBooleanProperty();

    @FXML
    private TableView<?> tb_Cliente;

    @FXML
    private TableColumn<ClientWrapper, String> col_razao;
    private BooleanProperty col_razao_enableFilter = new SimpleBooleanProperty();

    @FXML
    private TextField cx_cnpj;

    @FXML
    void acaoConfirmar(ActionEvent event) {

    }
    private void updateTable () {
        applyFilter();
    }



    private void applyFilter () {

        // System.out.println("applyFilter");

        List<Client> clientToDisplay = ClientFilter.filterFromView(
            loadedClient,
            Optional.empty(),
            Optional.empty()
        );

        displayedClient = FXCollections.observableArrayList(
            clientToDisplay.stream().map((Client cliente) -> new ClientWrapper(cliente)).collect(Collectors.toList())
        );

        tabela.setItems(displayedClient);
        tabela.refresh();

    }
    void inputClient () {
        QueryLibs.insertClient(new Client(
                cx_razao.getText(),
                cx_cnpj.getText()
            ));
            updateTable();
    }


    @FXML 
    private TableView<ClientWrapper> tabela;
    private ObservableList<ClientWrapper> displayedClient;
    private List<Client> loadedClient;


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
            new ColumnConfigString(col_razao, "razao", "Razão Social", Optional.of(col_razao_enableFilter)),
            new ColumnConfigString(col_cnpj, "cnpj", "CNPJ", Optional.of(col_razao_enableFilter))
        },
        Optional.of(applyFilterCallback)
    );

    
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buildTable();

        updateTable();    
    }
  
    
}
