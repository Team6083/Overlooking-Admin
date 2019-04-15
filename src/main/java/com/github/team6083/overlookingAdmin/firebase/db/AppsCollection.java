package com.github.team6083.overlookingAdmin.firebase.db;

import com.github.team6083.overlookingAdmin.firebase.CloudFirestore;
import com.github.team6083.overlookingAdmin.module.App;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class AppsCollection extends CloudFirestore {

    public static CollectionReference getCollection() {
        return db.collection("apps");
    }

    public static List<App> getAllApps() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        List<QueryDocumentSnapshot> documentSnapshots = querySnapshotApiFuture.get().getDocuments();

        Iterator it = documentSnapshots.iterator();

        List<App> list = new ArrayList<>();

        while (it.hasNext()){
            DocumentSnapshot documentSnapshot = (DocumentSnapshot) it.next();

            JSONObject json = new JSONObject(documentSnapshot.getData());
            App app = App.decodeJSON(json, documentSnapshot.getReference());

            list.add(app);
        }
        return list;
    }

    public static DocumentReference getAppRefByName(String name) throws ExecutionException, InterruptedException {
        return getCollection().whereEqualTo("name", name).get().get().getDocuments().get(0).getReference();
    }

    public static DocumentReference getAppRefByAppToken(String token) throws ExecutionException, InterruptedException {
        return getCollection().whereEqualTo("app_token", token).get().get().getDocuments().get(0).getReference();
    }

    public static App getApp(DocumentReference reference) throws ExecutionException, InterruptedException {
        DocumentSnapshot documentSnapshot = reference.get().get();
        if (!documentSnapshot.exists()) return null;

        Map<String, Object> data = documentSnapshot.getData();
        JSONObject jsonObject = new JSONObject(data);

        App app = App.decodeJSON(jsonObject, reference);
        return app;
    }

    public static void saveApp(App app) throws ExecutionException, InterruptedException {
        if (app.app_Doc == null) {
            getCollection().add(app.encodeJSON().toMap());
        } else{
            ApiFuture<WriteResult> future = app.app_Doc.set(app.encodeJSON().toMap());
            System.out.println("Update time : " + future.get().getUpdateTime());
        }
    }
}
