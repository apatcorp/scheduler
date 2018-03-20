package main;

import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import utility.ScreenHandler;


public class Main extends Application{
    public static Scene scene;

    public void start(Stage primaryStage) {

        setUserAgentStylesheet(STYLESHEET_MODENA);

        ScreenHandler screenHandler = ScreenHandler.getInstance();

        screenHandler.add("LoadingScreen", "fxml/loading_screen_view.fxml");
        screenHandler.add("Main", "fxml/main_scene_view.fxml");
        screenHandler.add("AppointmentDetails", "fxml/appointment_details_view.fxml");
        screenHandler.add("NewDailyRoutine", "fxml/new_daily_routines_view.fxml");
        screenHandler.add("NewAppointmentDetail", "fxml/new_appointment_detail_view.fxml");

        scene = new Scene(screenHandler.getScreenInfo("LoadingScreen").getPane(), 1280, 800);
        scene.getStylesheets().add("css/CustomStyle.css");

        primaryStage.setTitle("Scheduler");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
