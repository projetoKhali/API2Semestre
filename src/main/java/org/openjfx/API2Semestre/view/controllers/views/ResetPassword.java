package org.openjfx.api2semestre.view.controllers.views;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.database.QueryLibs;
import org.openjfx.api2semestre.utils.PasswordIncription;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ResetPassword {
    Authentication auth = new Authentication();

    @FXML private PasswordField tfCurrentPassword;
    @FXML private PasswordField tfNovaSenha;
    @FXML private PasswordField tfRepitaNovaSenha;

    @FXML void btConfirmar(ActionEvent event) {
        String currentPassword = tfCurrentPassword.getText();
        String novaSenha = tfNovaSenha.getText();
        String repitaNovaSenha = tfRepitaNovaSenha.getText();

        if (!verify(currentPassword, novaSenha, repitaNovaSenha)) {
            displayPopup(Alert.AlertType.ERROR, "Erro ao alterar senha", "A senha inserida está incorreta", "");
        }

        QueryLibs.updateUserPassword(Authentication.getCurrentUser().getId(), novaSenha);

        displayPopup(Alert.AlertType.INFORMATION, "", "Senha alterada com sucesso", "");
    }

    private boolean verify (
        String currentPassword,
        String novaSenha,
        String repitaNovaSenha
    ) {
        if (currentPassword == null || novaSenha == null || repitaNovaSenha == null) return false;
        if (currentPassword.isBlank() || novaSenha.isBlank() || repitaNovaSenha.isBlank()) return false;

        if(!novaSenha.equals(repitaNovaSenha)) return false;

        // verifica se a senha incriptada é a mesma da do banco
        return (!Authentication.getCurrentUser().getPassword().equals(PasswordIncription.encryptPassword(currentPassword)));
    }

    public void displayPopup (AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);

        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner((Stage) tfCurrentPassword.getScene().getWindow());

        alert.getDialogPane().setHeaderText(header);
        alert.getDialogPane().setContentText(message);

        // Optional<ButtonType> result = 
        alert.showAndWait();

        tfCurrentPassword.requestFocus();
        tfCurrentPassword.selectAll();

    }

}


