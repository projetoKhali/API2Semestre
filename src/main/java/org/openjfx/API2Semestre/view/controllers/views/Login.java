package org.openjfx.api2semestre.view.controllers.views;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.view.manager.ViewsManager;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class Login {

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
