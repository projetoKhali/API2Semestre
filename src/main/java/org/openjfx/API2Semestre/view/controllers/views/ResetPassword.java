package org.openjfx.api2semestre.view.controllers.views;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.User;
import org.openjfx.api2semestre.database.QueryLibs;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ResetPassword {
    Authentication auth = new Authentication();
    User user = new User();

    @FXML private TextField tfNovaSenha;
    @FXML private Button btConfirmar;
    @FXML private TextField tfRepitaNovaSenha;

    @FXML
    void btConfirmar(ActionEvent event) {
        try {
            if(auth.resetPasswordUser(tfNovaSenha.getText(), tfRepitaNovaSenha.getText())){
                QueryLibs.resetUserPassword(tfNovaSenha.getText(), user.getId());
            }
        } catch (Exception e){
            System.out.println("Erro ao alterar senha");
            e.printStackTrace();
        }
    }
}


