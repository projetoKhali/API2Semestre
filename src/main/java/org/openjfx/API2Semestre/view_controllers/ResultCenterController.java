package org.openjfx.api2semestre.view_controllers;

import java.util.List;

import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.custom_tags.LookupTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ResultCenterController {
    
    @FXML
    private TextField tf_codigo;

    @FXML
    private TextField tf_colaborador;

    @FXML
    private TextField tf_gestor;

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_sigla;

    // private AutoCompletionBinding<User> collaboratorSelect;
    private ObservableList<User> colaborators;

    public void initialize(){
        System.out.println("ResultCenterController initialize");
        // colaborators = FXCollections.observableArrayList(QueryLibs.executeSelect(
        //     User.class,
        //     new QueryParam<?>[0]
        // ));
        colaborators = FXCollections.observableArrayList(new User[] {
            new User("Fulano colaborador", Profile.Colaborador, List.of("oi", "oi2"), List.of()),
            new User("Fulano gestor", Profile.Gestor, List.of("oi3"), List.of("oi", "oi2")),
            new User("Fulano adm", Profile.Administrator, List.of(), List.of("oi3"))
        });
        // collaboratorSelect = TextFields.bindAutoCompletion(tf_colaborador, colaborators);

        LookupTextField<User> lutf_colaborador = new LookupTextField<User>(colaborators);
        ((HBox)tf_colaborador.getParent()).getChildren().set(
            ((HBox)tf_colaborador.getParent()).getChildren().indexOf(tf_colaborador),
            lutf_colaborador
        );
        tf_colaborador = lutf_colaborador;
        // Set an event handler for when a user is selected
        lutf_colaborador.setOnAction(event -> {
            User user = lutf_colaborador.getSelectedItem();
            System.out.println("User selected: " + user != null ? user.toString() : "NULL");
        });

        
    }


    @FXML
    void adicionarColaborador(ActionEvent event) {

    }

}
