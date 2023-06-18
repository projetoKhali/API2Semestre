package org.openjfx.api2semestre.view.controllers.views;

import org.openjfx.api2semestre.App;
import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.view.manager.ViewsManager;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Login {

    @FXML private TextField tf_email;
    @FXML private TextField tf_senha;

    public void initialize() {
        tf_senha.setOnKeyPressed(event -> {
            if (event.getCode().equals(javafx.scene.input.KeyCode.ENTER)) {
                login();
            }
        });
    }

    public void login() {
        try {
            String email = tf_email.getText();
            String password = tf_senha.getText();
            if (Authentication.login(email, password)) {
                App.closeWindow();
                App.changeView(ViewsManager.handleViews());
                App.getStage().show();
                App.centerWindow();
            } else displayErrorPopup();
        } catch (Exception e) {
            System.out.println("Erro ao efetuar login");
            e.printStackTrace();
        }
    }

    public void displayErrorPopup () {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro ao efetuar login");

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner((Stage) tf_senha.getScene().getWindow());

        alert.getDialogPane().setHeaderText("Email ou senha incorretos");
        alert.getDialogPane().setContentText(
            "Verifique as informações fornecidas."
        );

        // Optional<ButtonType> result = 
        alert.showAndWait();

        tf_senha.requestFocus();
        tf_senha.selectAll();

    }

}
