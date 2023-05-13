package org.openjfx.api2semestre.view_controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class UserRegisterController {

    @FXML private ComboBox<Profile> cbFunction;
    @FXML private TextField tfName;
    @FXML private TextField tfEmail;
    @FXML private TextField tfID;   
    @FXML private TableView<User> table;
    
    private ObservableList<Profile> obsFunctions;    
    
    @FXML
    private void register(ActionEvent event) {
        // Chamar função do back que irá fazer o insert dos dados na tabela
        // Os prints abaixo são só para validar o funcionamento dos inputs de dados
        Profile function = cbFunction.getSelectionModel().getSelectedItem();
        System.out.println(tfName.getText());
        System.out.println(tfEmail.getText());
        System.out.println(tfID.getText());
        System.out.println(function);
    }
    
    public void initialize() {
        
        loadFunction();
    }
    
    public void loadFunction() {
        obsFunctions = FXCollections.observableArrayList(Profile.PROFILES);
        cbFunction.setItems(obsFunctions);
    }      
}