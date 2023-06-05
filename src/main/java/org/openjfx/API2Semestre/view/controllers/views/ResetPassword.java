package org.openjfx.api2semestre.view.controllers.views;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.database.QueryLibs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ResetPassword {
    Authentication auth = new Authentication();

    @FXML private TextField tfNovaSenha;
    @FXML private TextField tfRepitaNovaSenha;

    @FXML void btConfirmar(ActionEvent event) {
        String novaSenha = tfNovaSenha.getText();
        String repitaNovaSenha = tfRepitaNovaSenha.getText();

        if (novaSenha == null || repitaNovaSenha == null) return;

        if(!novaSenha.equals(repitaNovaSenha)) return;

        QueryLibs.resetUserPassword(tfNovaSenha.getText(), Authentication.getCurrentUser().getId());
    }
}


