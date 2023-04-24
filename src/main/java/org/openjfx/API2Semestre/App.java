package org.openjfx.api2semestre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.openjfx.api2semestre.authentication.Profile;

public class App extends Application {

    private static final Profile access = Profile.Administrator;

    private static Scene scene;
    private static Stage stage;
    private static void setStage (Stage newStage) { stage = newStage; }

    @Override
    public void start(Stage stage) throws IOException {
        setStage(stage);

        scene = new Scene(loadFXML(
            (access == Profile.Administrator) ? "login_provisory_adm" : 
            (access == Profile.Gestor) ? "login_provisory_ges" : 
            "login_provisory_col" 
        ));

        // scene = new Scene(loadFXML("appointments"), 826, 400);
        // scene = new Scene(loadFXML("approvals"), 826, 400);
        // scene = new Scene(loadFXML("listagemAdm"), 826, 400);
        stage.setScene(scene);
        stage.show();
    }

    public static void changeView (String viewFxmlFile) {
        try {
            Parent root = loadFXML(viewFxmlFile);
            scene = new Scene(root);
            stage.setScene(scene);
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