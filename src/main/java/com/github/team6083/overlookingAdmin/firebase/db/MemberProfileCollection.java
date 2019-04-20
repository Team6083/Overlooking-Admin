package com.github.team6083.overlookingAdmin.firebase.db;

import com.github.team6083.overlookingAdmin.firebase.CloudFirestore;
import com.github.team6083.overlookingAdmin.module.App;
import com.github.team6083.overlookingAdmin.module.MemberProfile;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MemberProfileCollection extends CloudFirestore {
    public static CollectionReference getCollection() {
        return db.collection("member_profile");
    }

    public static List<MemberProfile> getAll() throws ExecutionException, InterruptedException {
        ApiFuture<QuerySnapshot> querySnapshotApiFuture = getCollection().get();

        List<QueryDocumentSnapshot> documentSnapshots = querySnapshotApiFuture.get().getDocuments();

        Iterator it = documentSnapshots.iterator();

        List<MemberProfile> list = new ArrayList<>();

        while (it.hasNext()){
            DocumentSnapshot documentSnapshot = (DocumentSnapshot) it.next();

            JSONObject json = new JSONObject(documentSnapshot.getData());
            MemberProfile memberProfile = MemberProfile.decodeJSON(json);

            memberProfile.documentReference = documentSnapshot.getReference();
            list.add(memberProfile);
        }
        return list;
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
        jsonObject.remove("config");

        jsonObject.put("config", documentSnapshot.get("config"));


        MemberProfile memberProfile = MemberProfile.decodeJSON(jsonObject);


        memberProfile.documentReference = reference;

        return memberProfile;
    }

    public static DocumentReference saveProfile(MemberProfile profile) throws ExecutionException, InterruptedException {
        if (profile.getFields() == null) {
            throw new Error("field config is empty");
        }

        return saveStorable(getCollection(), profile);
    }

}
