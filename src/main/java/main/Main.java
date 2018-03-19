package main;

import controller.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utility.ScreenHandler;

import java.io.IOException;


public class Main extends Application{
    public static Scene scene;

    public static boolean loadingScreenShown = false;

    public void start(Stage primaryStage) {
        ScreenHandler screenHandler = ScreenHandler.getInstance();

        screenHandler.add("LoadingScreen", "fxml/loading_screen_view.fxml");
        screenHandler.add("Main", "fxml/main_scene_view.fxml");
        screenHandler.add("AppointmentDetails", "fxml/appointment_details_view.fxml");
        screenHandler.add("NewDailyRoutine", "fxml/new_daily_routines_view.fxml");
        screenHandler.add("NewAppointmentDetail", "fxml/new_appointment_detail_view.fxml");

        scene = new Scene(screenHandler.getScreenInfo("LoadingScreen").getPane(), 1280, 800);

        primaryStage.setTitle("Scheduler");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
