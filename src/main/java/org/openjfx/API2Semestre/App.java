package org.openjfx.api2semestre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.view_controllers.BaseController;
import org.openjfx.api2semestre.view_controllers.templates.ViewButtonController;
import org.openjfx.api2semestre.views_manager.View;
import org.openjfx.api2semestre.views_manager.ViewsManager;

public class App extends Application {

    // mude o perfil de acesso para logar com diferentes permissões
    private static final Profile access = Profile.Colaborador;

    private static Scene scene;
    private static Stage stage;
    private static void setStage (Stage newStage) { stage = newStage; }

    private static String currentViewFxmlFile;

    private static BaseController baseController;

    @Override
    public void start(Stage stage) throws IOException {

        // QueryLibs.executeSqlFile("./SQL/tabelas.sql");
        // QueryLibs.executeSqlFile("./SQL/views.sql");

        setStage(stage);
        System.setProperty("javafx.fxml.debug", "true");
        stage.setScene(new Scene(loadFXML("views/approvals")));
        stage.show();

        // loginView();
    }

    public static void loginView () {

        currentViewFxmlFile = (
            access == Profile.Administrator ? "login/provisory_adm" : 
            access == Profile.Gestor ? "login/provisory_ges" : 
            "login/provisory_col"
        );
        
        try {
            scene = new Scene(loadFXML(currentViewFxmlFile));

            stage.setScene(scene);
            stage.show();

        } catch (Exception ex) {
            System.out.println("App.loginView() -- Erro!");
            ex.printStackTrace();
        }

    }

    static void loadBase () throws IOException {
        FXMLLoader loader = new FXMLLoader(App.getFXML("base.fxml"));
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

                FXMLLoader viewButtonLoader = new FXMLLoader(App.getFXML("templates/viewButtonTemplate.fxml"));

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
            System.out.println("App.changeView() -- Erro!");
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

        // QueryLibs.executeSqlFile("SQL/tabelas.sql");
        // QueryLibs.executeSqlFile("SQL/views.sql");

        // System.exit(1);

        launch();
    }

}