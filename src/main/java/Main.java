import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application{
    public static Scene scene;

    public void start(Stage primaryStage) {

        ScreenHandler screenHandler = ScreenHandler.getInstance();
        screenHandler.add("Main", "fxml/main_scene_view.fxml");
        screenHandler.add("AppointmentDetails", "fxml/appointment_details_view.fxml");
        screenHandler.add("NewDailyRoutine", "fxml/new_daily_routines_view.fxml");

        scene = new Scene(screenHandler.getScreenInfo("Main").getPane(), 1280, 800);

        primaryStage.setTitle("Scheduler");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
