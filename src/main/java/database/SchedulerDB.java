package database;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import data_structures.DailyRoutine;
import javafx.application.Platform;
import utility.Utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SchedulerDB {

    private static Firestore db;
    public static final String DB_NAME = "scheduler-bfb8b";

    public static Firestore getDB () {

        if (db == null) {
            db = initializeDB();
        }

        return db;
    }

    private static Firestore initializeDB () {
        Firestore db = null;

        try {
            InputStream inputStream = new FileInputStream("./scheduler-bfb8b.json");

            GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(credentials).build();

            FirebaseApp.initializeApp(options);

            db = FirestoreClient.getFirestore();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return db;
    }

    private void fetchAppointments (LocalDate localDate) {
        String documentID = Utility.getDocumentIDFromLocalDate(localDate);
        final ApiFuture<DocumentSnapshot> query = SchedulerDB.getDB().collection(SchedulerDB.DB_NAME).document(documentID).get();

        new Thread(() -> {
            try {
                DocumentSnapshot documentSnapshot = query.get();
                if (documentSnapshot != null) {
                    DailyRoutine dailyRoutine = documentSnapshot.toObject(DailyRoutine.class);

                    Platform.runLater(() -> {

                    });
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void fetchAllDailyRoutines () {
        ApiFuture<QuerySnapshot> query = SchedulerDB.getDB().collection(SchedulerDB.DB_NAME).get();

        new Thread(() -> {
            try {
                QuerySnapshot snapshots = query.get();
                List<QueryDocumentSnapshot> documents = snapshots.getDocuments();

                for (QueryDocumentSnapshot documentSnapshot: documents) {
                    DailyRoutine dailyRoutine = documentSnapshot.toObject(DailyRoutine.class);
                    LocalDate date = Utility.localDateFromAppointmentDate(dailyRoutine.getAppointmentDate());

                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

        }).start();
    }

}
