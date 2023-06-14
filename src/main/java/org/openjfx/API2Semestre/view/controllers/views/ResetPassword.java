package org.openjfx.api2semestre.view.controllers.views;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.database.QueryLibs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

public class ResetPassword {
    Authentication auth = new Authentication();

    @FXML private PasswordField tfNovaSenha;
    @FXML private PasswordField tfRepitaNovaSenha;

    @FXML void btConfirmar(ActionEvent event) {
        String novaSenha = tfNovaSenha.getText();
        String repitaNovaSenha = tfRepitaNovaSenha.getText();

        if (novaSenha == null || repitaNovaSenha == null) return;

        if(!novaSenha.equals(repitaNovaSenha)) return;

        QueryLibs.updateUserPassword(Authentication.getCurrentUser().getId(), tfNovaSenha.getText());
    }
}


