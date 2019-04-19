package com.github.team6083.overlookingAdmin.firebase.db;

import com.github.team6083.overlookingAdmin.firebase.CloudFirestore;
import com.github.team6083.overlookingAdmin.module.MemberProfile;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MemberProfileCollection extends CloudFirestore {
    public static CollectionReference getCollection() {
        return db.collection("member_profile");
    }

    public static MemberProfile getProfile(String uid) throws ExecutionException, InterruptedException {
        DocumentReference documentReference = getCollection().document(uid);

        return getProfile(documentReference);
    }

    public static MemberProfile getProfile(DocumentReference reference) throws ExecutionException, InterruptedException {
        DocumentSnapshot documentSnapshot = reference.get().get();
        if (!documentSnapshot.exists()) return null;

        Map<String, Object> data = documentSnapshot.getData();

        JSONObject jsonObject = new JSONObject(data);

        MemberProfile memberProfile = MemberProfile.decodeJSON(jsonObject);
        return memberProfile;
    }

}
