package org.openjfx.api2semestre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

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

        // Para criar as tabelas que estiverem faltando
        org.openjfx.api2semestre.database.QueryLibs.executeSqlFile("SQL/tabelas.sql");
        // Para criar as views que estiverem faltando
        org.openjfx.api2semestre.database.QueryLibs.executeSqlFile("SQL/views.sql");

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
        baseController = null;
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

        if (scene != null && baseController != null) scene = new Scene(loader.load(), scene.getWidth(), scene.getHeight());
        else scene = new Scene(loader.load());
        stage.setScene(scene);

        baseController = loader.getController();

        Label lb_currentUser = baseController.getLb_currentUser();
        lb_currentUser.setText("Logado como " + Authentication.getCurrentUser().getNome());
        lb_currentUser.setWrapText(true);

    }

    public static void changeView (Optional<String> newViewFxmlOptional) {
        try {

            loadBase();

            if (newViewFxmlOptional.isPresent()) {
                String newViewFxml = newViewFxmlOptional.get();
                Parent module = loadFXML(newViewFxml);
                baseController.getAp_content().getChildren().add(module);
                currentViewFxmlFile = newViewFxml;

                for (View view : ViewsManager.getViews()) {
    
                    FXMLLoader viewButtonLoader = new FXMLLoader(App.getFXML("templates/viewButtonTemplate"));
    
                    baseController.getVb_views().getChildren().add(viewButtonLoader.load());
    
                    ViewButtonController viewButtonTemplateController = viewButtonLoader.getController();
    
                    String buttonViewFxmlFile = view.getFxmlFileName();
    
                    viewButtonTemplateController.setView(buttonViewFxmlFile);
                    viewButtonTemplateController.setText(view.getDisplayName());
    
                    if (buttonViewFxmlFile.equals(currentViewFxmlFile)) viewButtonTemplateController.setDisable(false);
                    if (buttonViewFxmlFile.equals(newViewFxml)) viewButtonTemplateController.setDisable(true);
                }

            }

        } catch (Exception ex) {
            System.out.println("App.changeView() -- Erro ao trocar para a tela '" + newViewFxmlOptional + "'");
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