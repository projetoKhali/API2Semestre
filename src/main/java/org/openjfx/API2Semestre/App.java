package org.openjfx.api2semestre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.view_controllers.BaseController;
import org.openjfx.api2semestre.view_controllers.templates.ViewButtonController;
import org.openjfx.api2semestre.views_manager.View;
import org.openjfx.api2semestre.views_manager.ViewsManager;

public class App extends Application {

    // mude o perfil de acesso para logar com diferentes permissões
    // private static final Profile access = Profile.Colaborador;
    // private static final Profile access = Profile.Gestor;
    private static final Profile access = Profile.Administrator;

    private static Scene scene;
    private static Stage stage;

    public static Stage getStage () { return stage; }
    private static void setStage (Stage newStage) { stage = newStage; }

    private static String currentViewFxmlFile;

    private static BaseController baseController;

    @Override
    public void start(Stage stage) throws IOException {
        setStage(stage);

        // QueryLibs.executeSqlFile("./SQL/tabelas.sql");
        // QueryLibs.executeSqlFile("./SQL/views.sql");


        stage.setScene(new Scene(loadFXML("views/report")));
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
        FXMLLoader loader = new FXMLLoader(App.class.getResource("base.fxml"));
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

                FXMLLoader viewButtonLoader = new FXMLLoader(App.class.getResource("templates/viewButtonTemplate.fxml"));

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

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {

        // // verbas teste
        // IntervalFee[] verbas = new IntervalFee[] {

        //     // sem restrição de período / verba base
        //     new IntervalFee(1000, 1.00f, Week.ALL.get(), 0, 0, 0, false),

        //     // final de semana (sabado e domingo)
        //     new IntervalFee(1001, 1.25f, Week.FDS.get(), 0, 0, 0, false),

        //     // qualquer dia, noturno | cumulativo
        //     new IntervalFee(1002, 1.47f, Week.ALL.get(), 22, 6, 0, true),

        //     // qualquer dia, após 2 horas de hora-extra | cumulativo
        //     new IntervalFee(1002, 2.00f, Week.ALL.get(), 0, 0, 2, true)
        // };

        // // exemplo
        // double sum = 0;
        // for (int i = 0; i < testTimestamps.length; i++) {
        //     Timestamp[] start_end = testTimestamps[i];
        //     for (IntervalFee verba : verbas) {
        //         System.out.println("["+i+"] verba " + verba.getCode() + " | verificando :)");

        //         if (verba.check(start_end[0], start_end[1], sum) ) {
        //             System.out.println("["+i+"] verba " + verba.getCode() + " aplica-se a " + start_end[0].toString() + " e " + start_end[1].toString());
        //         }
        //         else System.out.println("["+i+"] verba " + verba.getCode() + " NÃO se aplica a " + start_end[0].toString() + " e " + start_end[1].toString());
        //         // sum += total;
        //     }
        // }
        // System.exit(1);

        // System.setProperty("javafx.fxml.debug", "true");
        launch();
    }

}