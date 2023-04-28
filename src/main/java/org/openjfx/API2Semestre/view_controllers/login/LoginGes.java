package org.openjfx.api2semestre.view_controllers.login;

import java.io.IOException;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.authentication.User;

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
            if (Authentication.login(new User(
                tf_name.getText(),
                Profile.Gestor,
                ProvisoryLogin.parseSquads(tf_member.getText()),
                ProvisoryLogin.parseSquads(tf_manages.getText())

            ))) {
                App.changeView(ProvisoryLogin.handleViews());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
