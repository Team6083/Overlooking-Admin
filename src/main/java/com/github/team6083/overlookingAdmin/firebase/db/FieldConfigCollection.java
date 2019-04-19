package com.github.team6083.overlookingAdmin.firebase.db;

import com.github.team6083.overlookingAdmin.firebase.CloudFirestore;
import com.github.team6083.overlookingAdmin.module.FieldConfig;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FieldConfigCollection extends CloudFirestore {

    public static CollectionReference getCollection() {
        return db.collection("field_config");
    }

    public static FieldConfig getConfig(String uid) throws ExecutionException, InterruptedException {
        DocumentReference reference = getCollection().document(uid);

        return getConfig(reference);
    }

    public static FieldConfig getConfig(DocumentReference reference) throws ExecutionException, InterruptedException {
        DocumentSnapshot documentSnapshot = reference.get().get();
        if (!documentSnapshot.exists()) return null;

        Map<String, Object> data = documentSnapshot.getData();

        JSONObject jsonObject = new JSONObject(data);

        FieldConfig config = FieldConfig.decodeJSON(jsonObject, reference);

        return config;
    }

}
