package org.openjfx.api2semestre.view.controllers.views;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.view.macros.ColumnConfig;
import org.openjfx.api2semestre.view.macros.ColumnConfigString;
import org.openjfx.api2semestre.view.macros.TableMacros;
import org.openjfx.api2semestre.view.utils.filters.UserFilter;
import org.openjfx.api2semestre.view.utils.interfaces.EditableTableView;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import org.openjfx.api2semestre.view.macros.TableMacros.Formatter;

public class Users implements EditableTableView<User> {

    @FXML private ComboBox<Profile> cb_profile;
    @FXML private TextField tf_name;
    @FXML private TextField tf_email;
    @FXML private TextField tf_matricula;   

    @FXML private TableView<User> tabela;

    @FXML private TableColumn<User, String> col_matricula;
    @FXML private TableColumn<User, String> col_name;
    @FXML private TableColumn<User, String> col_email;
    @FXML private TableColumn<User, Profile> col_function;

    private BooleanProperty col_matricula_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_name_enableFilter = new SimpleBooleanProperty();
    private BooleanProperty col_email_enableFilter = new SimpleBooleanProperty();

    private ObservableList<User> displayedUsers;
    private List<User> loadedUsers;

    public void initialize() {
        buildTable();
        updateTable();
    }

    @SuppressWarnings("unchecked") private void buildTable () {

        cb_profile.setItems(FXCollections.observableArrayList(Profile.PROFILES));
        cb_profile.setValue(Profile.Colaborador);

        ChangeListener<Boolean> applyFilterCallback = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                applyFilter();
            }
        };

        col_function.setCellValueFactory( new PropertyValueFactory<>( "profile" ));

        TableMacros.<User, String>enableEditableCells(
            col_matricula,
            (String value) -> !value.isBlank(),
            (User item, String value) -> item.setMatricula(value),
            Formatter.DEFAULT_STRING_FORMATTER
        );

        TableMacros.<User, String>enableEditableCells(
            col_name,
            (String value) -> !value.isBlank(),
            (User item, String value) -> item.setNome(value),
            Formatter.DEFAULT_STRING_FORMATTER
        );

        TableMacros.<User, String>enableEditableCells(
            col_email,
            (String value) -> !value.isBlank(),
            (User item, String value) -> item.setEmail(value),
            Formatter.DEFAULT_STRING_FORMATTER
        );

        TableMacros.<User, Profile>enableEditableCells(
            col_function,
            (Profile value) -> true,
            (User item, Profile value) -> item.setPerfil(value),
            new Formatter<Profile>() {
                private final StringConverter<Profile> converter = null;
                @Override public String format(Profile value, boolean editing) { return value.getName(); }
                @Override public String parse(String text) { return text; }
                @Override public StringConverter<Profile> getConverter() { return converter; }
            }
        );

        TableMacros.<User>createDeleteColumn(tabela, "usuário", (User user) -> {
            QueryLibs.deleteUser(user);
            updateTable();
        });

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

    @FXML private void register(ActionEvent event) {
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

    @Override @FXML public void saveChanges(ActionEvent event) {
        tabela.getItems().stream().forEach((User user) -> QueryLibs.updateUser(user));
    }

}