package org.openjfx.api2semestre.view.controllers.views;

import org.openjfx.api2semestre.data.ResultCenter;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view.controllers.custom_tags.LookupTextField;
import org.openjfx.api2semestre.view.controllers.popups.ResultCenterEdit;
import org.openjfx.api2semestre.view.macros.ColumnConfig;
import org.openjfx.api2semestre.view.macros.ColumnConfigString;
import org.openjfx.api2semestre.view.macros.TableMacros;
import org.openjfx.api2semestre.view.utils.filters.ResultCenterFilter;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.openjfx.api2semestre.authentication.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class ResultCenters {

    @FXML private TextField tf_codigo;
    @FXML private TextField tf_colaborador;
    @FXML private TextField tf_gestor;
    @FXML private TextField tf_name;
    @FXML private TextField tf_sigla;

    @FXML private FlowPane fp_colaboradores;
    private LinkedList<User> selectedUsers = new LinkedList<User>();

    @FXML private TableView<ResultCenter> tabela;

    @FXML private TableColumn<ResultCenter, String> col_nome;
    @FXML private TableColumn<ResultCenter, String> col_sigla;
    @FXML private TableColumn<ResultCenter, String> col_codigo;
    @FXML private TableColumn<ResultCenter, String> col_gestor;

    private BooleanProperty col_nome_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_sigla_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_codigo_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_gestor_enableFilter = new SimpleBooleanProperty();

    // @FXML private TableColumn<ResultCenter, String> col_membros;
    // private BooleanProperty col_membros_enableFilter = new SimpleBooleanProperty();

    private ObservableList<ResultCenter> displayedResultCenters;
    private List<ResultCenter> loadedResultCenters;

    public void initialize(){

        buildTable();
        updateTable();

        // Query all users from database. TODO: implement pagination
        User[] users = QueryLibs.selectAllUsers();

        // Create a new LookupTextField to replace the default javafx TextField at tf_colaborador
        LookupTextField<User> lookupTextFieldColaborador = new LookupTextField<User>(users);
        ((HBox)tf_colaborador.getParent()).getChildren().set(
            ((HBox)tf_colaborador.getParent()).getChildren().indexOf(tf_colaborador),
            lookupTextFieldColaborador
        );
        tf_colaborador = lookupTextFieldColaborador;

        // Create a new LookupTextField to replace the default javafx TextField at tf_gestor
        // In this case, filter the users array to only show level 1 (gestor) and 2 (adm) profile for the suggestions
        LookupTextField<User> lookupTextFieldGestor = new LookupTextField<User>(
            (User[])Arrays.stream(users).filter((User user) -> user.getProfile().getProfileLevel() > 0).toArray(User[]::new)
        );
        ((HBox)tf_gestor.getParent()).getChildren().set(
            ((HBox)tf_gestor.getParent()).getChildren().indexOf(tf_gestor),
            lookupTextFieldGestor
        );

        // Add an additional callback on the selectedUser property of tf_gestor 
        // to enable/disable the User selected at the LookupTextField on changed
        lookupTextFieldGestor.selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override public void changed(ObservableValue<? extends User> arg0, User oldPropertyValue, User newPropertyValue) {
                if (oldPropertyValue != null) lookupTextFieldColaborador.addSuggestion(oldPropertyValue);
                if (newPropertyValue != null) lookupTextFieldColaborador.removeSuggestion(newPropertyValue);
            }
        });
        tf_gestor = lookupTextFieldGestor;
    }

    @SuppressWarnings("unchecked") private void buildTable () {

        ChangeListener<Boolean> applyFilterCallback = new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                applyFilter();
            }
        };

        // col_selecionar.setCellValueFactory( new PropertyValueFactory<>( "selected" ));
        // TableCheckBoxMacros.setCheckBoxHeader(tabela, col_selecionar);

        TableMacros.buildTable(
            tabela,
            new ColumnConfig[] {
                new ColumnConfigString<>(col_nome, "name", "Nome", Optional.of(col_nome_enableFilter)),
                new ColumnConfigString<>(col_sigla, "acronym", "Sigla", Optional.of(col_sigla_enableFilter)),
                new ColumnConfigString<>(col_codigo, "code", "Codigo", Optional.of(col_codigo_enableFilter)),
                new ColumnConfigString<>(col_gestor, "managerName", "Gestor", Optional.of(col_gestor_enableFilter)),
            },
            Optional.of(applyFilterCallback)
        );

        TableMacros.<ResultCenter, ResultCenterEdit>createEditPopupColumn(
            tabela, 
            "Centro de Resultado",
            "popups/resultCenterEdit",
            Optional.of((ResultCenter resultCenter) -> {
                QueryLibs.deleteResultCenter(resultCenter.getId());
                updateTable();
            })
        );
    }

    private void updateTable () {
        ResultCenter[] items = QueryLibs.selectAllResultCenters();
        System.out.println(items.length + " resultCenters returned from select ");

        loadedResultCenters = Arrays.asList(items);
        displayedResultCenters =  FXCollections.observableArrayList(loadedResultCenters);

        tabela.setItems(displayedResultCenters);
    }

    private void applyFilter () {

        // System.out.println("applyFilter");

        List<ResultCenter> appointmentsToDisplay = ResultCenterFilter.filterFromView(
            loadedResultCenters,
            col_nome_enableFilter.get() ? Optional.of(col_nome) : Optional.empty(),
            col_sigla_enableFilter.get() ? Optional.of(col_sigla) : Optional.empty(),
            col_codigo_enableFilter.get() ? Optional.of(col_codigo) : Optional.empty(),
            col_gestor_enableFilter.get() ? Optional.of(col_gestor) : Optional.empty()
        );

        displayedResultCenters = FXCollections.observableArrayList(appointmentsToDisplay);

        tabela.setItems(displayedResultCenters);
        tabela.refresh();

        // col_selecionar.setGraphic(null);
        // TableCheckBoxMacros.setCheckBoxHeader(tabela, col_selecionar);
    }

    @FXML void adicionarColaborador(ActionEvent event) {

        // get the TextFields as LookupTextField
        @SuppressWarnings("unchecked") LookupTextField<User> lookupTfColaborador = ((LookupTextField<User>)tf_colaborador);
        @SuppressWarnings("unchecked") LookupTextField<User> lookupTfGestor = ((LookupTextField<User>)tf_gestor);

        // get the selectedUser of the LoopkupTextField
        User selectedUser = lookupTfColaborador.getSelectedItem();

        // if null, cancel
        if (selectedUser == null) return;

        // Add the user to the selectedUsers List so that when the ResultCenter is inserted,
        // all the selected users can be assigned the member_cr relation to the ResultCenter inserted
        selectedUsers.add(selectedUser);

        // Remove the suggestion from both LookupTextFields and clear the contents of lookupTfColaborador
        lookupTfGestor.removeSuggestion(selectedUser);
        lookupTfColaborador.removeSuggestion(selectedUser);
        lookupTfColaborador.clear();

        // Create a label with the User's name
        Label userLabel = new Label(selectedUser.getName());
        HBox.setMargin(userLabel, new Insets(0, 0, 0, 0));
        userLabel.setPadding(new Insets(0));
        userLabel.setFont(Font.font("Arial", 12));

        // Create a button to remove the user from the FlowPane
        Button userRemoveButton = new Button("X");
        HBox.setMargin(userRemoveButton, new Insets(0, 0, 0, 4));
        userRemoveButton.setPadding(new Insets(0, 4, 0, 4));
        userRemoveButton.setMinHeight(18);
        userRemoveButton.setMaxHeight(18);
        userRemoveButton.setPrefHeight(18);
        userRemoveButton.setAlignment(Pos.CENTER);
        userRemoveButton.setStyle("-fx-background-radius: 16;");

        // Create an HBox to wrap both 
        HBox userContainer = new HBox(userLabel, userRemoveButton);
        userContainer.setPadding(new Insets(2));
        userContainer.setAlignment(Pos.CENTER_LEFT);

        // Add the container to the FlowPane
        fp_colaboradores.getChildren().add(userContainer);

        // Add OnAction to the button to remove the user from selectedUsers, 
        userRemoveButton.setOnAction(e -> {
            fp_colaboradores.getChildren().remove(userContainer);
            lookupTfColaborador.addSuggestion(selectedUser);
            if (selectedUser.getProfile().getProfileLevel() > 0) lookupTfGestor.addSuggestion(selectedUser);
            selectedUsers.remove(selectedUser);
        });

    }

    @FXML void cadastrarCentro (ActionEvent event) {

        // get the TextFields as LookupTextField
        @SuppressWarnings("unchecked") LookupTextField<User> lookupTfcolaborador = ((LookupTextField<User>)tf_colaborador);
        @SuppressWarnings("unchecked") LookupTextField<User> lookupTfgestor = ((LookupTextField<User>)tf_gestor);

        User gestor = lookupTfgestor.getSelectedItem();

        String name = tf_name.getText();
        String sigla = tf_sigla.getText();
        String codigo = tf_codigo.getText();

        if (gestor == null || name.isBlank() || sigla.isBlank() || codigo.isBlank()) {
            System.out.println("ResultCentersController.cadastrarCentro() -- Erro: Dado não informado!");
            return;
        }

        // insert the new ResultCenter and store the id
        int cr_id = QueryLibs.insertResultCenter(new ResultCenter(
            name,
            sigla,
            codigo,
            gestor.getId()
        ));

        // add the membro_cr relation between each selectedUser and thhe new ResultCenter
        for (User selectedUser : selectedUsers) {
            QueryLibs.addUserToResultCenter(selectedUser.getId(), cr_id);

            // enable the selectedUser back to the suggestions of both LookupTextFields
            lookupTfcolaborador.addSuggestion(selectedUser);
            if (selectedUser.getProfile().getProfileLevel() > 0) lookupTfgestor.addSuggestion(selectedUser);
        }

        // clear the values of each TextField
        tf_name.clear();
        tf_sigla.clear();
        tf_codigo.clear();

        // clear the values of each LookupTextField
        lookupTfcolaborador.clear();
        lookupTfgestor.clear();

        // clear the FlowPane
        fp_colaboradores.getChildren().clear();

        // atualiza a tabela para incluir o ResultCenter inserido
        updateTable();
    }
}
