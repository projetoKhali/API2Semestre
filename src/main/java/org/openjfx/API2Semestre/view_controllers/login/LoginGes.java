package org.openjfx.api2semestre.view_controllers.login;

import java.io.IOException;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.authentication.Authentication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LoginGes {

    @FXML
    private TextField tf_manages;

    @FXML
    private TextField tf_member;

    @FXML
    private TextField tf_name;

    @FXML
    private void login (ActionEvent event) throws IOException {
        try {
            if (Authentication.login("exemplo@email.com","0")) {
                App.changeView(ProvisoryLogin.handleViews());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
