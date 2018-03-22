package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application{
    public static Scene scene;

    public void start(Stage primaryStage) {
        setUserAgentStylesheet(STYLESHEET_MODENA);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/loading_screen_view.fxml"));
        try {
            Pane pane = fxmlLoader.load();
            scene = new Scene(pane, 1280, 800);
            scene.getStylesheets().add("css/CustomStyle.css");

            primaryStage.setTitle("Scheduler");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {


        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
