package org.openjfx.api2semestre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.view_controllers.BaseController;
import org.openjfx.api2semestre.view_controllers.ViewButtonController;
import org.openjfx.api2semestre.views_manager.View;
import org.openjfx.api2semestre.views_manager.ViewsManager;

public class App extends Application {

    // mude o perfil de acesso para logar com diferentes permissões
    private static final Profile access = Profile.Colaborador;

    private static Scene scene;
    private static Stage stage;
    private static void setStage (Stage newStage) { stage = newStage; }

    private static BaseController baseController;

    @Override
    public void start(Stage stage) throws IOException {
        setStage(stage);

        loginView();
    }

    public static void loginView () {
        try {
            scene = new Scene(loadFXML(
                (access == Profile.Administrator) ? "login_provisory_adm" : 
                (access == Profile.Gestor) ? "login_provisory_ges" : 
                "login_provisory_col" 
            ));

            stage.setScene(scene);
            stage.show();

        } catch (Exception ex) {
            System.out.println("App.loginView() -- Erro!");
            ex.printStackTrace();
        }

    }

    static void loadBase () throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("base.fxml"));
        stage.setScene(new Scene(loader.load()));
        baseController = loader.getController();

        baseController.getLb_currentUser().setText("Logado como " + Authentication.getCurrentUser().getNome());
    }

    public static void changeView (String viewFxmlFile) {
        try {

            loadBase();

            Parent module = loadFXML(viewFxmlFile);
            baseController.getAp_content().getChildren().add(module);
    
            for (View view : ViewsManager.getViews()) {
                FXMLLoader viewButtonLoader = new FXMLLoader(App.class.getResource("viewButtonTemplate.fxml"));
                // baseController.getVb_views().getChildren().clear();
                baseController.getVb_views().getChildren().add(viewButtonLoader.load());
                ViewButtonController viewButtonTemplateController = viewButtonLoader.getController();
                viewButtonTemplateController.setView(view.getFxmlFileName());
                viewButtonTemplateController.setText(view.getDisplayName());

            }

            stage.show();

        } catch (Exception ex) {
            System.out.println("App.changeView() -- Erro!");
            ex.printStackTrace();
        }
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        System.setProperty("javafx.fxml.debug", "true");
        launch();
    }

}