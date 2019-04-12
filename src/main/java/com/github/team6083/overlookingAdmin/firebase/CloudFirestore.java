package com.github.team6083.overlookingAdmin.firebase;

import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;

public class CloudFirestore {

    protected static Firestore db;

    public static void init(){
        db = FirestoreClient.getFirestore();
    }

    public static CollectionReference getUsersCollection(){
        return UsersCollection.getCollection();
    }

}
