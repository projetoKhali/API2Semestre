package org.openjfx.api2semestre.view.controllers.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.appointment.Appointment;
import org.openjfx.api2semestre.appointment.Status;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.data.ResultCenter;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view.controllers.popups.Justification;
import org.openjfx.api2semestre.view.controllers.templates.ApprovePopupListItem;
import org.openjfx.api2semestre.view.controllers.templates.RejectPopupListItem;
import org.openjfx.api2semestre.view.macros.ColumnConfig;
import org.openjfx.api2semestre.view.macros.ColumnConfigString;
import org.openjfx.api2semestre.view.macros.TableCheckBoxMacros;
import org.openjfx.api2semestre.view.macros.TableMacros;
import org.openjfx.api2semestre.view.utils.filters.AppointmentFilter;
import org.openjfx.api2semestre.view.utils.interfaces.Popup;
import org.openjfx.api2semestre.view.utils.interfaces.PopupListCallback;
import org.openjfx.api2semestre.view.utils.wrappers.AppointmentWrapper;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Approvals {

    @FXML private TableColumn<AppointmentWrapper, Boolean> col_selecionar;
    @FXML private TableColumn<AppointmentWrapper, String> col_requester;
    @FXML private TableColumn<AppointmentWrapper, String> col_squad;
    @FXML private TableColumn<AppointmentWrapper, String> col_tipo;
    @FXML private TableColumn<AppointmentWrapper, String> col_inicio;
    @FXML private TableColumn<AppointmentWrapper, String> col_fim;
    @FXML private TableColumn<AppointmentWrapper, String> col_cliente;
    @FXML private TableColumn<AppointmentWrapper, String> col_projeto;
    @FXML private TableColumn<AppointmentWrapper, String> col_total;
    @FXML private TableColumn<AppointmentWrapper, Void> col_justificativa;

    private BooleanProperty col_requester_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_squad_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_tipo_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_inicio_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_fim_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_cliente_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_projeto_enableFilter = new SimpleBooleanProperty();

    @FXML private TableView<AppointmentWrapper> tabela;
    private ObservableList<AppointmentWrapper> displayedAppointments;
    private List<Appointment> loadedAppointments;

    public void initialize() {

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
                new ColumnConfigString<>(col_squad, "resultCenter", "CR", Optional.of(col_squad_enableFilter)),
                new ColumnConfigString<>(col_tipo, "type", "Tipo", Optional.empty()),
                new ColumnConfigString<>(col_inicio, "startDate", "Data Início", Optional.empty()),
                new ColumnConfigString<>(col_fim, "endDate", "Data Fim", Optional.empty()),
                new ColumnConfigString<>(col_cliente, "client", "Cliente", Optional.of(col_cliente_enableFilter)),
                new ColumnConfigString<>(col_projeto, "project", "Projeto", Optional.of(col_projeto_enableFilter)),
                new ColumnConfigString<>(col_total, "total", "Total", Optional.empty())
            },
            Optional.of(applyFilterCallback)
        );

        col_justificativa.setCellFactory(column -> new TableCell<AppointmentWrapper, Void>() {
            private final Button button = new Button("Ver");
            {button.setOnAction(event -> showJustificationPopup(getTableView().getItems().get(getIndex()))); }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(button);
            }
        });
    }

    private void updateTable () {
        List<Appointment> items = new ArrayList<>();

        // Authentication.login(new User("jhow", Profile.Gestor, "e@xem.plo", "teste", "teste"));

        // Inicia a conexão com o banco de dados
        Optional<java.sql.Connection> connectionOptional = QueryLibs.connect();

        for (ResultCenter resultCenter : QueryLibs.selectResultCentersManagedBy(
            Authentication.getCurrentUser().getId(),
            connectionOptional
        )) {

            // System.out.println("resultCenter: " + resultCenter);
            for(Appointment apt : QueryLibs.selectAppointmentsOfResultCenter(
                resultCenter.getId(),
                connectionOptional
            )) {

                // System.out.println("apt: " + apt);
                // try {
                items.add(apt);
                // } catch (Exception e) {
                //     e.printStackTrace();
                // }
            }
        }

        // Fecha a conexão com o banco de dados
        QueryLibs.close(connectionOptional);

        loadedAppointments = items;

        applyFilter();
    }

    private void applyFilter () {

        // System.out.println("applyFilter");

        List<Appointment> appointmentsToDisplay = AppointmentFilter.filterFromView(
            loadedAppointments,
            col_requester_enableFilter.get() ? Optional.of(col_requester) : Optional.empty(),
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

        col_selecionar.setGraphic(null);
        TableCheckBoxMacros.setCheckBoxHeader(tabela, col_selecionar);
    }

    private void showJustificationPopup (AppointmentWrapper apt) {

        try {
            // Create a new stage for the popup
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Justificativa de " + apt.getType() + " do(a) " + apt.getRequester());

            // Load the FXML file for the list item
            FXMLLoader loader = new FXMLLoader(App.getFXML("popups/justification"));
            AnchorPane popup = loader.load();
            ((Justification)loader.getController()).setAppointment(apt);

            // Create a scene for the popup
            Scene scene = new Scene(popup, 800, 400);
            popupStage.setScene(scene);
            popupStage.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private List<AppointmentWrapper> getSelected () {
        return displayedAppointments.stream().filter((AppointmentWrapper apt) -> apt.getSelected()).collect(Collectors.toList());
    }

    @FXML private void showApprovePopup (ActionEvent event) throws IOException {
        System.out.println("showApprovePopup");
        createPopup(
            "templates/approvePopupListItem",
            "Aprovar",
            (List<ApprovePopupListItem> controllers) -> {
                System.out.println("showApprovePopup callback");
                Optional<java.sql.Connection> connectionOptional = QueryLibs.connect();
                for (ApprovePopupListItem controller : controllers) {
                    Appointment appointment = controller.getSelected().getAppointment();
                    appointment.setStatus(1);
                    QueryLibs.updateAppointment(appointment);
                    // System.out.println("Apontamento atualizado");
                }
                QueryLibs.close(connectionOptional);
                updateTable();
            }
        );
    }

    @FXML private void showRejectPopup (ActionEvent event) throws IOException {
        System.out.println("showRejectPopup");
        createPopup(
            "templates/rejectPopupListItem",
            "Rejeitar",
            (List<RejectPopupListItem> controllers) -> {
                // System.out.println("showRejectPopup callback");
                List<Appointment> appointments = new ArrayList<>();
                for (RejectPopupListItem controller : controllers) {
                    String feedback = controller.getFeedback();
                    if (feedback == null || feedback.isEmpty()) {
                        System.out.println("feedback não preenchido");
                        return;
                    }
                    Appointment appointment = controller.getSelected().getAppointment();
                    appointment.setFeedback(feedback);
                    appointment.setStatus(2);
                    appointments.add(appointment);
                }
                Optional<java.sql.Connection> connectionOptional = QueryLibs.connect();                
                for (Appointment appointment : appointments) {
                    QueryLibs.updateAppointment(appointment);
                    // System.out.println("Apontamento atualizado");
                }
                QueryLibs.close(connectionOptional);
                updateTable();
            }
        );
    }

    private <T extends Popup<AppointmentWrapper>> Stage createPopup (
        String popupFxmlFile,
        String actionText,
        PopupListCallback<T> callback
    ) {

        // Get the list of items from the main application
        List<AppointmentWrapper> selectedAppointments = getSelected();

        // Create a new stage for the popup
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setTitle(actionText + " " + selectedAppointments.size() + " apontamentos selecionados");

        // Create a VBox to hold the HBox controls for each item
        VBox itemsVBox = new VBox();
        itemsVBox.setPrefWidth(-1);
        itemsVBox.setPrefHeight(-1);
        itemsVBox.setSpacing(8);
        itemsVBox.setAlignment(Pos.TOP_RIGHT);
        itemsVBox.setPadding(new Insets(16));

        List<T> controllers = selectedAppointments.stream().map((AppointmentWrapper aptWrapper) -> {
            try {
                // Load the FXML file for the list item
                FXMLLoader loader = new FXMLLoader(App.getFXML(popupFxmlFile));
                VBox listItem = loader.load();
                T controller = loader.getController();

                controller.setSelected(aptWrapper);

                // Add the HBox to the VBox
                itemsVBox.getChildren().add(listItem);

                return controller;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());

        // Create a scroll pane to hold the VBox
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(-1);
        scrollPane.setPrefHeight(-1);
        scrollPane.setContent(itemsVBox);
        scrollPane.setFitToWidth(true);

        // Create a button to close the popup
        Button actionButton = new Button(actionText);
        actionButton.setOnAction(event -> {
            callback.handlePopupList(controllers);
            popupStage.close();
        });

        // Create a VBox to hold the scroll pane and close button
        VBox root = new VBox(scrollPane, actionButton);
        root.setPrefWidth(-1);
        root.setPrefHeight(-1);

        // Create a scene for the popup
        Scene scene = new Scene(root, 800, 400);
        popupStage.setScene(scene);
        popupStage.showAndWait();

        return popupStage;
    }

}