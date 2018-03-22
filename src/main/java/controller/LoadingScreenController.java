package controller;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import data_structures.DailyRoutine;
import database.SchedulerDB;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LoadingScreenController extends Controller implements Initializable {

    @FXML
    public StackPane root;

    @FXML
    public ProgressIndicator progress;

    @FXML
    private Text infoText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setOpacity(0);
        progress.setOpacity(0);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), root);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.setCycleCount(1);

        fadeIn.setOnFinished(event -> {
            FadeTransition progressFadeIn = new FadeTransition(Duration.seconds(.25), progress);
            progressFadeIn.setFromValue(0);
            progressFadeIn.setToValue(1);
            progressFadeIn.setCycleCount(1);

            progressFadeIn.setOnFinished(event1 -> fetchAllDailyRoutines());

            progressFadeIn.play();
        });

        fadeIn.play();
    }

    private void fetchAllDailyRoutines () {
        new Thread(() -> {
            try {
                ApiFuture<QuerySnapshot> future = SchedulerDB.getDB().collection(SchedulerDB.DB_NAME).get();
                List<QueryDocumentSnapshot> documentSnapshots = future.get(5, TimeUnit.MINUTES).getDocuments();

                List<DailyRoutine> dailyRoutines = new ArrayList<>();

                for (QueryDocumentSnapshot documentSnapshot: documentSnapshots) {
                    DailyRoutine dailyRoutine = documentSnapshot.toObject(DailyRoutine.class);
                    dailyRoutines.add(dailyRoutine);
                }

                Platform.runLater(() -> onDataReceived(dailyRoutines));
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
                errorLoadingData();
            }
        }).start();
    }

    private void onDataReceived (List<DailyRoutine> dailyRoutines) {
        infoText.setText("Herzlich Willkommen");
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(.75), root);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);

        fadeOut.setOnFinished(event -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/main_scene_view.fxml"));
            try {
                Pane main = fxmlLoader.load();
                Controller controller = fxmlLoader.getController();
                controller.setup(dailyRoutines);

                root.getChildren().setAll(main);

                FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), root);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.setCycleCount(1);

                fadeIn.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        fadeOut.play();
    }

    private void errorLoadingData () {
        progress.setOpacity(0);
        infoText.setText("Fehler ist aufgetreten:\n Verbindung zum Server konnte nicht hergestellt werden\nVersuchen Sie es später erneut");
    }
}
