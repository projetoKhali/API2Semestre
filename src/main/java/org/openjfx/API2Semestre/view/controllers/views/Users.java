package org.openjfx.api2semestre.view.controllers.views;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.database.SQLConnection;
import org.openjfx.api2semestre.view.macros.ColumnConfig;
import org.openjfx.api2semestre.view.macros.ColumnConfigString;
import org.openjfx.api2semestre.view.macros.TableMacros;
import org.openjfx.api2semestre.view.utils.filters.UserFilter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.openjfx.api2semestre.view.macros.TableMacros.Formatter;

public class Users {

    @FXML private ComboBox<Profile> cb_profile;
    @FXML private TextField tf_name;
    @FXML private TextField tf_email;
    @FXML private TextField tf_matricula;   

    @FXML private TableView<User> tabela;

    @FXML private TableColumn<User, String> col_matricula;
    @FXML private TableColumn<User, String> col_name;
    @FXML private TableColumn<User, String> col_email;
    @FXML private TableColumn<User, Profile> col_function;
    @FXML private AnchorPane confirmation;

    private BooleanProperty col_matricula_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_name_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_email_enableFilter = new SimpleBooleanProperty();

    private ObservableList<User> displayedUsers;
    private List<User> loadedUsers;
    
    public void initialize() {
    
        buildTable();

        updateTable();
        
        TableMacros.<User, String>enableEditableCells(
            col_matricula,
            (String value) -> ! value.isBlank(),
            (User item, String value) -> item.setMatricula(value),
            new Formatter<String>() {
                private final StringConverter<String> converter = null;
                @Override public String format(String value, boolean editing) { return value; }
                @Override public String parse(String text) { return text; }
                @Override public StringConverter<String> getConverter() { return converter; }
            }
        );

        TableMacros.<User, String>enableEditableCells(
            col_name,
            (String value) -> ! value.isBlank(),
            (User item, String value) -> item.setNome(value),
            new Formatter<String>() {
                private final StringConverter<String> converter = null;
                @Override public String format(String value, boolean editing) { return value; }
                @Override public String parse(String text) { return text; }
                @Override public StringConverter<String> getConverter() { return converter; }
            }
        );

        TableMacros.<User, String>enableEditableCells(
            col_email,
            (String value) -> ! value.isBlank(),
            (User item, String value) -> item.setEmail(value),
            new Formatter<String>() {
                private final StringConverter<String> converter = null;
                @Override public String format(String value, boolean editing) { return value; }
                @Override public String parse(String text) { return text; }
                @Override public StringConverter<String> getConverter() { return converter; }
            }
        );

        TableMacros.<User, Profile>enableEditableCells(
            col_function,
            (Profile value) -> true,
            (User item, Profile value) -> item.setPerfil(value),
            new Formatter<Profile>() {
                private final StringConverter<Profile> converter = null;
                @Override public String format(Profile value, boolean editing) { return value.getDisplayName(); }
                @Override public String parse(String text) { return text; }
                @Override public StringConverter<Profile> getConverter() { return converter; }
            }
        );

    }

    @SuppressWarnings("unchecked")
    private void buildTable () {

        TableColumn<User, Void> buttonColumn = new TableColumn<>("Opção");
        tabela.getColumns().add(buttonColumn);

        buttonColumn.setCellFactory(param -> new TableCell<>() {
            
            private final Button button = new Button("Deletar");

            {
                button.setOnAction(event -> {
                    try {
                        deleteUser();
                    } catch (IOException | SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                });
            }
        });

        cb_profile.setItems(FXCollections.observableArrayList(Profile.PROFILES));
        cb_profile.setValue(Profile.Colaborador);

        ChangeListener<Boolean> applyFilterCallback = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                applyFilter();
            }
        };

        col_function.setCellValueFactory( new PropertyValueFactory<>( "profile" ));

        TableMacros.buildTable(
            tabela,
            new ColumnConfig[] {
                new ColumnConfigString<>(col_name, "name", "Nome", Optional.of(col_name_enableFilter)),
                new ColumnConfigString<>(col_email, "email", "Email", Optional.of(col_email_enableFilter)),
                new ColumnConfigString<>(col_matricula, "registration", "Matricula", Optional.of(col_matricula_enableFilter)),
            },
            Optional.of(applyFilterCallback)
        );
    }

    private void updateTable () {
        User[] items = QueryLibs.selectAllUsers();

        System.out.println(items.length + " users returned from select ");
    
        loadedUsers = Arrays.asList(items);

        applyFilter();
    }

    private void applyFilter () {

        // System.out.println("applyFilter");

        List<User> usersToDisplay = UserFilter.filterFromView(
            loadedUsers,
            col_matricula_enableFilter.get() ? Optional.of(col_matricula) : Optional.empty(),
            col_name_enableFilter.get() ? Optional.of(col_name) : Optional.empty(),
            col_email_enableFilter.get() ? Optional.of(col_email) : Optional.empty()
        );

        displayedUsers = FXCollections.observableArrayList(
            // usersToDisplay.stream().map((User apt) -> new UserWrapper(apt)).collect(Collectors.toList())
            usersToDisplay
        );

        tabela.setItems(displayedUsers);
        tabela.refresh();

    }
    
    private void register(ActionEvent event) {
        Profile profile = cb_profile.getSelectionModel().getSelectedItem();
        String name = tf_name.getText();
        String email = tf_email.getText();
        String matricula = tf_matricula.getText();

        if (name.isEmpty() || email.isEmpty() || matricula.isEmpty()) {
            System.out.println("Campo inválido");
            return;
        }

        QueryLibs.insertUser(new User(
            name,
            profile,
            email,
            matricula
        ));

        updateTable();
    }
    
    private void deleteUser() throws IOException, SQLException {
        
        Stage stage = (Stage) confirmation.getScene().getWindow();
        
        Alert.AlertType type = Alert.AlertType.CONFIRMATION;
        Alert alert = new Alert(type, "");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(stage);

        alert.getDialogPane().setContentText("Tem certeza que deseja excluir este usuário?");
        alert.getDialogPane().setHeaderText("Excluir usuário: ");

        Optional<ButtonType> result = alert.showAndWait();
        
        if(result.get() == ButtonType.OK) {
        
            User selectedUser = tabela.getSelectionModel().getSelectedItem();

            QueryLibs.deleteUser(selectedUser); 
        }
    }   
}