package org.openjfx.api2semestre.view_controllers;

import org.openjfx.api2semestre.custom_tags.LookupTextField;
import org.openjfx.api2semestre.data.ResultsCenter;

import java.util.List;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ResultCenterController {

    @FXML private TextField tf_codigo;
    @FXML private TextField tf_colaborador;
    @FXML private TextField tf_gestor;
    @FXML private TextField tf_name;
    @FXML private TextField tf_sigla;

    @FXML private ListView<ResultsCenter> lv_colaboradores;
    
    @FXML private TableView<ResultsCenter> tabela;
    @FXML private TableColumn<ResultsCenter, Integer> col_id;
    @FXML private TableColumn<ResultsCenter, String> col_nome;
    @FXML private TableColumn<ResultsCenter, String> col_sigla;
    @FXML private TableColumn<ResultsCenter, String> col_codigo;
    @FXML private TableColumn<ResultsCenter, User> col_gestor;
    @FXML private TableColumn<ResultsCenter, Integer> col_membros;

    public void initialize(){
        System.out.println("ResultCenterController initialize");
        // colaborators = FXCollections.observableArrayList(QueryLibs.executeSelect(
        //     User.class,
        //     new QueryParam<?>[0]
        // ));
        User[] users = new User[] {
            new User("Fulano colaborador", Profile.Colaborador, List.of("oi", "oi2"), List.of()),
            new User("Fulano gestor", Profile.Gestor, List.of("oi3"), List.of("oi", "oi2")),
            new User("Cicrano col", Profile.Colaborador, List.of(), List.of("oi3")),
            new User("Cicrano ges", Profile.Gestor, List.of(), List.of("oi3")),
            new User("Cicrano adm", Profile.Administrator, List.of(), List.of("oi3"))
        };

        // tf_colaborador = AutocompleteTextField.setupAutocomplete(tf_colaborador, ((HBox)tf_colaborador.getParent()), users);
        LookupTextField lookupTextField = new LookupTextField(users);
        ((HBox)tf_colaborador.getParent()).getChildren().set(
            ((HBox)tf_colaborador.getParent()).getChildren().indexOf(tf_colaborador),
            lookupTextField
        );
        tf_colaborador = lookupTextField;
        // System.out.println(users.size() + " users");
        // // Set an event handler for when a user is selected
        // lutf_colaborador.setOnAction(event -> {
        //     User user = lutf_colaborador.getSelectedItem();
        //     System.out.println("User selected: " + (user == null ? "NULL" : user.toString()));
        // });


    }


    @FXML
    void adicionarColaborador(ActionEvent event) {
        LookupTextField lookupTfColaborador = ((LookupTextField)tf_colaborador);
        User selectedUser = lookupTfColaborador.getSelectedItem();
        System.out.println(selectedUser.getNome());
        
        User[] suggestions = lookupTfColaborador.getSuggestions();
        User[] newSuggestions = new User[suggestions.length - 1];
        int index = 0;
        for (User user : suggestions) {
            if (user.equals(selectedUser)) continue;
            newSuggestions[index] = user;
            index++;
        }
        lookupTfColaborador.setSuggestions(newSuggestions);
        lookupTfColaborador.clear();
        
        
    }

}
