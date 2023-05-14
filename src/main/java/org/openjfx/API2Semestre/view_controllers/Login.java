package org.openjfx.api2semestre.view_controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Login {

    @FXML private TextField login;
    
    @FXML private TextField senha;
   
    public void getLogin() throws IOException {
        String text = login.getText();
        String password = senha.getText();  
        /*
        Chamar back passando login e senha supondo uma classe UserLogin e um método Login
        UserLogin enterLogin = new UserLogin();
        enterLogin.Login(text, password);
        */
    }
}
