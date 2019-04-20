package com.github.team6083.overlookingAdmin.firebase;

import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.github.team6083.overlookingAdmin.module.StorableModule;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;

import java.util.concurrent.ExecutionException;

public class CloudFirestore {

    protected static Firestore db;

    public static void init(){
        db = FirestoreClient.getFirestore();
    }

    public static DocumentReference saveStorable(CollectionReference collection, StorableModule module) throws ExecutionException, InterruptedException {
        if (module.getDocumentReference() == null) {
            ApiFuture<DocumentReference> future = collection.add(module.getStorableMap());
            return future.get();
        } else {
            ApiFuture<WriteResult> future = module.getDocumentReference().set(module.getStorableMap());
            System.out.println("Update time : " + future.get().getUpdateTime());
            return module.getDocumentReference();
        }
    }
}
