package org.openjfx.api2semestre.view_controllers;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.authentication.Authentication;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BaseController {

    @FXML private VBox ap_content;

    @FXML private Label lb_currentUser;

    @FXML private VBox vb_lateral;

    @FXML private VBox vb_views;

    @FXML void logout(ActionEvent event) {
        Authentication.logout();
        App.loginView();
    }

    public VBox getAp_content() {
        return ap_content;
    }

    public Label getLb_currentUser() {
        return lb_currentUser;
    }

    public VBox getVb_views() {
        return vb_views;
    }

}
