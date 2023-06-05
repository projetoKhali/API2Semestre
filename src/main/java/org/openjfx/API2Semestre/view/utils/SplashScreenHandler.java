// package org.openjfx.api2semestre.view.utils;

// import org.openjfx.api2semestre.App;

// import javafx.application.Platform;
// import javafx.concurrent.Task;
// import javafx.scene.Scene;
// import javafx.scene.image.Image;
// import javafx.scene.image.ImageView;
// import javafx.scene.layout.StackPane;
// import javafx.stage.Stage;
// import javafx.stage.StageStyle;

// public class SplashScreenHandler {

//     private static final String SPLASH_IMAGE = "imagens/logoKhali.png";
//     private static final int SPLASH_WIDTH = 600;
//     private static final int SPLASH_HEIGHT = 400;
//     private static Scene splashScene;

//     @FunctionalInterface public interface AfterSplashScreenCallback {
//         void execute();
//     }    

//     public static void showSplashScreen(AfterSplashScreenCallback after) {

//         Thread thread = new Thread(() -> {
//             Image splashImage = new Image(App.class.getResource(SPLASH_IMAGE).toString());
//             ImageView splashImageView = new ImageView(splashImage);
//             StackPane splashLayout = new StackPane(splashImageView);
//             splashScene = new Scene(splashLayout, SPLASH_WIDTH, SPLASH_HEIGHT);
//             Stage splashStage = new Stage();
        
//             splashStage.initStyle(StageStyle.UNDECORATED);
//             splashStage.setScene(splashScene);
//             splashStage.show();
//         });

//         System.out.println("oi antes do thread.start()");

//         thread.start();
//         try {
//             System.out.println("oi antes");
//             Thread.sleep(1000);
//             System.out.println("oi depois");
//             thread.join();
//         } catch (InterruptedException e) {
//             e.printStackTrace();
//         }
    
//         // Perform the long-running operation
//         after.execute();
//         System.out.println("oi after.execute()");
    
//         Stage splashStage = (Stage) splashScene.getWindow();
//         splashStage.close();


//     }
// }
