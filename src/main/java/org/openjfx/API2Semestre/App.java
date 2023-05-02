package org.openjfx.api2semestre;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;

import org.openjfx.api2semestre.authentication.Authentication;
import org.openjfx.api2semestre.authentication.Profile;
import org.openjfx.api2semestre.report.IntervalFee;
import org.openjfx.api2semestre.report.Week;
import org.openjfx.api2semestre.view_controllers.BaseController;
import org.openjfx.api2semestre.view_controllers.templates.ViewButtonController;
import org.openjfx.api2semestre.views_manager.View;
import org.openjfx.api2semestre.views_manager.ViewsManager;

public class App extends Application {

    // mude o perfil de acesso para logar com diferentes permissões
    private static final Profile access = Profile.Administrator;

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

        loginView();
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

        // verbas teste
        IntervalFee[] verbas = new IntervalFee[] {

            // sem restrição de período / verba base
            new IntervalFee(1000, 1.00f, Week.ALL.get(), 0, 0, 0, false),

            // final de semana (sabado e domingo)
            new IntervalFee(1001, 1.25f, Week.FDS.get(), 0, 0, 0, false),

            // qualquer dia, noturno | cumulativo
            new IntervalFee(1002, 1.47f, Week.ALL.get(), 22, 6, 0, true),

            // qualquer dia, após 2 horas de hora-extra | cumulativo
            new IntervalFee(1002, 2.00f, Week.ALL.get(), 0, 0, 2, true)
        };

        Timestamp[][] testTimestamps = new Timestamp[][] {
            new Timestamp[] {
                new Timestamp(2023, 4, 30, 11, 0, 0, 0),
                new Timestamp(2023, 4, 30, 12, 0, 0, 0)
            },
            new Timestamp[] {
                new Timestamp(2023, 5, 1, 11, 0, 0, 0),
                new Timestamp(2023, 5, 1, 12, 0, 0, 0)
            },
            new Timestamp[] {
                new Timestamp(2023, 5, 1, 23, 30, 0, 0),
                new Timestamp(2023, 5, 2, 0, 30, 0, 0)
            }
        };

        // exemplo
        double sum = 0;
        for (int i = 0; i < testTimestamps.length; i++) {
            Timestamp[] start_end = testTimestamps[i];
            for (IntervalFee verba : verbas) {
                System.out.println("["+i+"] verba " + verba.getCode() + " | verificando :)");

                if (verba.check(start_end[0], start_end[1], sum) ) {
                    System.out.println("["+i+"] verba " + verba.getCode() + " aplica-se a " + start_end[0].toString() + " e " + start_end[1].toString());
                }
                else System.out.println("["+i+"] verba " + verba.getCode() + " NÃO se aplica a " + start_end[0].toString() + " e " + start_end[1].toString());
                // sum += total;
            }
        }
        System.exit(1);

        // System.setProperty("javafx.fxml.debug", "true");
        // launch();

        System.out.print("oi");
        String str_t1 = "2023-04-27 12:00";
        String str_t1_final = "2023-04-28 12:00";
        Timestamp timestamp_t1 = Timestamp.valueOf(str_t1);
        Timestamp timestamp_t1_final = Timestamp.valueOf(str_t1_final);

        String str_t2 = "2023-04-28 08:00";
        String str_t2_final = "2023-04-28 13:00";
        Timestamp timestamp_t2 = Timestamp.valueOf(str_t2);
        Timestamp timestamp_t2_final = Timestamp.valueOf(str_t2_final);

        LocalDateTime t1 = timestamp_t1.toLocalDateTime();
        LocalDateTime t1_final = timestamp_t1_final.toLocalDateTime();
        LocalDateTime t2 = timestamp_t2.toLocalDateTime();
        LocalDateTime t2_final = timestamp_t2_final.toLocalDateTime();

        if (t1_final.isBefore(t1) || t2_final.isBefore(t2)) {
            System.out.println("Not proper intervals");
        } else {
            long numberOfOverlappingDates;
            if (t1_final.isBefore(t2) || t2_final.isBefore(t1)) {
                // no overlap
                numberOfOverlappingDates = 0;
            } else {
                LocalDateTime laterStart = Collections.max(Arrays.asList(t1, t2));
                LocalDateTime earlierEnd = Collections.min(Arrays.asList(t1_final, t2_final));
                numberOfOverlappingDates = ChronoUnit.DAYS.between(laterStart, earlierEnd);
            }
            System.out.println("" + numberOfOverlappingDates + " days of overlap");
        }

    }

}