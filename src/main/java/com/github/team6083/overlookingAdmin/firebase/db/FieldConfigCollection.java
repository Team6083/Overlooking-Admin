package com.github.team6083.overlookingAdmin.firebase.db;

import com.github.team6083.overlookingAdmin.firebase.CloudFirestore;
import com.github.team6083.overlookingAdmin.module.FieldConfig;
import com.github.team6083.overlookingAdmin.module.MemberProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FieldConfigCollection extends CloudFirestore {

    public static CollectionReference getCollection() {
        return db.collection("field_config");
    }

    public static List<FieldConfig> getAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        List<QueryDocumentSnapshot> documentSnapshots = querySnapshotApiFuture.get().getDocuments();

        Iterator it = documentSnapshots.iterator();

        List<FieldConfig> list = new ArrayList<>();

        while (it.hasNext()){
            DocumentSnapshot documentSnapshot = (DocumentSnapshot) it.next();

            JSONObject json = new JSONObject(documentSnapshot.getData());

            FieldConfig fieldConfig = FieldConfig.decodeJSON(json, documentSnapshot.getReference());

            list.add(fieldConfig);
        }
        return list;
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

    public static DocumentReference saveConfig(FieldConfig fieldConfig) throws ExecutionException, InterruptedException {
       return saveStorable(getCollection(), fieldConfig);
    }

}
