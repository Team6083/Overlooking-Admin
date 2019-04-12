package com.github.team6083.overlookingAdmin.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.IOException;
import java.io.InputStream;

public class FirebaseMain {
    private static FirebaseOptions options;
    private static String dbUrl;

    public static void init(InputStream serviceAccount, String db) throws IOException {
        dbUrl = db;
        FirestoreOptions firestoreOptions = FirestoreOptions.newBuilder().
                setTimestampsInSnapshotsEnabled(true)
                .build();

        options = new FirebaseOptions.Builder()
                .setFirestoreOptions(firestoreOptions)
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl(dbUrl)
                .build();
        FirebaseApp.initializeApp(options);

        CloudFirestore.init();
    }

    public static String getDbUrl(){
        return dbUrl;
    }

    public static FirebaseOptions getOptions(){
        return options;
    }
}
