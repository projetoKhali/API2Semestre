package org.openjfx.api2semestre.view.controllers.popups;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import org.openjfx.api2semestre.data.ResultCenter;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view.controllers.custom_tags.LookupTextField;
import org.openjfx.api2semestre.view.macros.TableMacros;
import org.openjfx.api2semestre.view.macros.TableMacros.Formatter;
import org.openjfx.api2semestre.view.utils.interfaces.EditPopup;
import org.openjfx.api2semestre.view.utils.interfaces.PopupCallback;
import org.openjfx.api2semestre.authentication.User;

public class ResultCenterEdit implements EditPopup<ResultCenter> {

    @FXML private TextField tf_colaborador;

    private LookupTextField<User> lookupTextFieldColaborador;
    private LookupTextField<User> lookupTextFieldGestor;

    @FXML private FlowPane fp_members;
    private LinkedList<User> selectedUsers = new LinkedList<User>();

    @FXML private TableView<ResultCenter> tabela;

    @FXML private TableColumn<ResultCenter, String> col_nome;
    @FXML private TableColumn<ResultCenter, String> col_sigla;
    @FXML private TableColumn<ResultCenter, String> col_codigo;
    @FXML private TableColumn<ResultCenter, String> col_gestor;

    private PopupCallback<ResultCenter> saveCallback;

    @Override public void setSelected(ResultCenter resultCenter) {

        User[] users = QueryLibs.selectAllUsers();

        // Create a new LookupTextField to replace the default javafx TextField at tf_colaborador
        lookupTextFieldColaborador = new LookupTextField<User>(users);
        ((HBox)tf_colaborador.getParent()).getChildren().set(
            ((HBox)tf_colaborador.getParent()).getChildren().indexOf(tf_colaborador),
            lookupTextFieldColaborador
        );
        tf_colaborador = lookupTextFieldColaborador;

        col_nome.setCellValueFactory( new PropertyValueFactory<>( "name" ));
        col_sigla.setCellValueFactory( new PropertyValueFactory<>( "acronym" ));
        col_codigo.setCellValueFactory( new PropertyValueFactory<>( "code" ));

        TableMacros.<ResultCenter, String>enableEditableCells(
            col_nome,
            (String value) -> !value.isBlank(),
            (ResultCenter item, String value) -> tabela.getItems().get(0).setName(value),
            Formatter.DEFAULT_STRING_FORMATTER
        );
        TableMacros.<ResultCenter, String>enableEditableCells(
            col_sigla,
            (String value) -> !value.isBlank(),
            (ResultCenter item, String value) -> tabela.getItems().get(0).setAcronym(value),
            Formatter.DEFAULT_STRING_FORMATTER
        );
        TableMacros.<ResultCenter, String>enableEditableCells(
            col_codigo,
            (String value) -> !value.isBlank(),
            (ResultCenter item, String value) -> tabela.getItems().get(0).setCode(value),
            Formatter.DEFAULT_STRING_FORMATTER
        );

        tabela.setItems(FXCollections.observableArrayList(List.of(resultCenter)));

        col_gestor.setCellFactory(column -> new TableCell<ResultCenter, String>() {
            private LookupTextField<User> lookupTextField;

            {
                User[] users = QueryLibs.selectAllManagersAndAdms();
                lookupTextField = new LookupTextField<User>(
                    (User[])Arrays.stream(users).filter((User user) -> user.getProfile().getProfileLevel() > 0).toArray(User[]::new)
                );
                lookupTextField.setPromptText(resultCenter.getManagerName());
                try {
                    User currentManager = QueryLibs.selectUserById(resultCenter.getManagerId()).orElseThrow(() -> new Exception());
                    lookupTextField.selectedItemProperty().addListener(new ChangeListener<User>() {
                        @Override public void changed(ObservableValue<? extends User> arg0, User oldPropertyValue, User newPropertyValue) {
                            if (oldPropertyValue != null) lookupTextFieldColaborador.addSuggestion(oldPropertyValue);
                            if (newPropertyValue != null) lookupTextFieldColaborador.removeSuggestion(newPropertyValue);
                        }
                    });
                    // lookupTextField.setText(currentManager.getName());
                    lookupTextField.selectedItemProperty().set(currentManager);
                    lookupTextFieldColaborador.removeSuggestion(currentManager);

                } catch (Exception e) {
                    System.out.println("Gestor não encontrado!");
                    e.printStackTrace();
                }
                setGraphic(lookupTextField);
                lookupTextFieldGestor = lookupTextField;
            }
        });

        Arrays.asList(QueryLibs.selectMembersOfResultCenter(resultCenter.getId())).stream().forEach((User user) -> addUser(user));
    }

    @FXML void adicionarColaborador(ActionEvent event) {

        // get the selectedUser of the LoopkupTextField
        User selectedUser = lookupTextFieldColaborador.getSelectedItem();

        // if null, cancel
        if (selectedUser == null) return;

        addUser(selectedUser);

        lookupTextFieldColaborador.clear();

    }

    private void addUser (User user) {
        // Add the user to the selectedUsers List so that when the ResultCenter is inserted,
        // all the selected users can be assigned the member_cr relation to the ResultCenter inserted
        selectedUsers.add(user);

        // Remove the suggestion from both LookupTextFields and clear the contents of lookupTextFieldColaborador
        lookupTextFieldGestor.removeSuggestion(user);
        lookupTextFieldColaborador.removeSuggestion(user);

        // Create a label with the User's name
        Label userLabel = new Label(user.getName());
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
        fp_members.getChildren().add(userContainer);

        // Add OnAction to the button to remove the user from selectedUsers, 
        userRemoveButton.setOnAction(e -> {
            fp_members.getChildren().remove(userContainer);
            lookupTextFieldColaborador.addSuggestion(user);
            if (user.getProfile().getProfileLevel() > 0) lookupTextFieldGestor.addSuggestion(user);
            selectedUsers.remove(user);
        });
    }

    @Override public ResultCenter getSelected() { return tabela.getItems().get(0); }
    @Override public TableView<ResultCenter> getTable() { return tabela; }

    @Override public void setSaveCallback(PopupCallback<ResultCenter> callback) {
        this.saveCallback = callback;
    }

    @FXML private void save (ActionEvent e) throws IOException {
        if (saveCallback != null) saveCallback.execute(tabela.getItems().get(0));

        int cr_id = tabela.getItems().get(0).getId();

        QueryLibs.removeAllusersFromResultCenter(cr_id);

        // add the membro_cr relation between each selectedUser and thhe new ResultCenter
        for (User selectedUser : selectedUsers) {
            QueryLibs.addUserToResultCenter(selectedUser.getId(), cr_id);
        }

        close(e);
    }

    @FXML private void close (ActionEvent e) throws IOException {
        ((Stage) tabela.getScene().getWindow()).close();
    }

}