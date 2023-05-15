package org.openjfx.api2semestre.view_controllers;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.views_manager.ViewsManager;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField tf_email;
    @FXML private TextField tf_senha;
   
    public void login() {
        try {
            String email = tf_email.getText();
            String password = tf_senha.getText();
            if (Authentication.login(email, password)) {
                App.changeView(ViewsManager.handleViews());
            }
        } catch (Exception e) {
            System.out.println("Erro ao efetuar login");
            e.printStackTrace();
        }
    }
}
