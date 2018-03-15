package database;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(credentials)
                    .build();

            FirebaseApp.initializeApp(options);

            db = FirestoreClient.getFirestore();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return db;
    }
}
