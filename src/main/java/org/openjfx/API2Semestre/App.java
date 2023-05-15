package org.openjfx.api2semestre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.view_controllers.BaseController;
import org.openjfx.api2semestre.view_controllers.templates.ViewButtonController;
import org.openjfx.api2semestre.views_manager.View;
import org.openjfx.api2semestre.views_manager.ViewsManager;

public class App extends Application {
    
    private static Scene scene;
    private static Stage stage;

    public static Stage getStage () { return stage; }
    private static void setStage (Stage newStage) { stage = newStage; }
    
    private static String currentViewFxmlFile;
    
    private static BaseController baseController;
    
    @Override
    public void start(Stage stage) throws IOException {

        // org.openjfx.api2semestre.database.QueryLibs.insertUser(new org.openjfx.api2semestre.authentication.User(
        //     "humano adm exemplo",
        //     org.openjfx.api2semestre.authentication.Profile.Administrator,
        //     "a@d.m",                             <---------------------------- LOGIN
        //     "123"                                <---------------------------- SENHA
        // ));
        // org.openjfx.api2semestre.database.QueryLibs.insertUser(new org.openjfx.api2semestre.authentication.User(
        //     "humano ges exemplo",
        //     org.openjfx.api2semestre.authentication.Profile.Gestor,
        //     "g@e.s",                             <---------------------------- LOGIN
        //     "123"                                <---------------------------- SENHA
        // ));
        // org.openjfx.api2semestre.database.QueryLibs.insertUser(new org.openjfx.api2semestre.authentication.User(
        //     "humano adm exemplo",
        //     org.openjfx.api2semestre.authentication.Profile.Colaborador,
        //     "c@o.l",                             <---------------------------- LOGIN
        //     "123"                                <---------------------------- SENHA
        // ));

        setStage(stage);
        loginView();
    }

    public static void loginView () {
        try {
            scene = new Scene(loadFXML((currentViewFxmlFile = "views/login")));
            stage.setScene(scene);
            stage.show();
            System.out.println(currentViewFxmlFile);

        } catch (Exception ex) {
            System.out.println("App.loginView() -- Erro!");
            ex.printStackTrace();
        }

    }

    static void loadBase () throws IOException {
        FXMLLoader loader = new FXMLLoader(App.getFXML("base"));
        stage.setScene(new Scene(loader.load()));
        baseController = loader.getController();

        baseController.getLb_currentUser().setText("Logado como " + Authentication.getCurrentUser().getNome());
    }

    public static void changeView (String newViewFxmlFile) {
        try {

            loadBase();

            Parent module = loadFXML(newViewFxmlFile);
            baseController.getAp_content().getChildren().add(module);
    
            for (View view : ViewsManager.getViews()) {

                FXMLLoader viewButtonLoader = new FXMLLoader(App.getFXML("templates/viewButtonTemplate"));

                baseController.getVb_views().getChildren().add(viewButtonLoader.load());

                ViewButtonController viewButtonTemplateController = viewButtonLoader.getController();

                String buttonViewFxmlFile = view.getFxmlFileName();

                viewButtonTemplateController.setView(buttonViewFxmlFile);
                viewButtonTemplateController.setText(view.getDisplayName());

                if (buttonViewFxmlFile.equals(currentViewFxmlFile)) viewButtonTemplateController.setDisable(false);
                if (buttonViewFxmlFile.equals(newViewFxmlFile)) viewButtonTemplateController.setDisable(true);

            }

            currentViewFxmlFile = newViewFxmlFile;

        } catch (Exception ex) {
            System.out.println("App.changeView() -- Erro ao trocar para a tela '" + newViewFxmlFile + "'");
            ex.printStackTrace();
        }
    }

    public static URL getFXML (String fxml) {
        return App.class.getResource(fxml + ".fxml");
    }

    private static Parent loadFXML(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getFXML(fxml));
            return fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.setProperty("javafx.fxml.debug", "true");
        launch();
    }

}