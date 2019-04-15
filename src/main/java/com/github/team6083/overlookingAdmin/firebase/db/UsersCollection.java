package com.github.team6083.overlookingAdmin.firebase.db;

import com.github.team6083.overlookingAdmin.firebase.Auth;
import com.github.team6083.overlookingAdmin.firebase.CloudFirestore;
import com.github.team6083.overlookingAdmin.module.User;
import com.github.team6083.overlookingAdmin.util.Converter;
import com.github.team6083.overlookingAdmin.util.RandomGenerator;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class UsersCollection extends CloudFirestore {

    public static CollectionReference getCollection() {
        return db.collection("users");
    }

    public static DocumentReference getUserRefByUid(String uid) {
        return getCollection().document(uid);
    }

    public static DocumentReference getUserRefByEmail(String email) throws ExecutionException, InterruptedException {
        return getCollection().whereEqualTo("email", email).get().get().getDocuments().get(0).getReference();
    }

    public static void saveUser(User user) throws InterruptedException, ExecutionException, FirebaseAuthException {
        saveUser(user, null);
    }

    public static User getUser(UserRecord userRecord) throws InterruptedException, ExecutionException, ParseException, FirebaseAuthException {
        return getUser(getUserRefByUid(userRecord.getUid()), userRecord);
    }

    public static User getUser(DocumentReference reference, UserRecord userRecord) throws ParseException, ExecutionException, InterruptedException, FirebaseAuthException {
        DocumentSnapshot documentSnapshot = reference.get().get();
        if (!documentSnapshot.exists()) return null;

        Map<String, Object> data = documentSnapshot.getData();
        data.remove("birthDay");
        Timestamp timestamp = documentSnapshot.getTimestamp("birthDay");
        Date d = new Date();
        if (timestamp != null) {
            d = Converter.googleTimestampToDate(timestamp);
        }

        JSONObject jsonObject = new JSONObject(data);
        jsonObject.put("birthDay", d);

        User user = User.decodeJSON(jsonObject, userRecord);
        if (userRecord == null) {
            userRecord = Auth.getUserByEmail(user.email);
        }
        user.firebaseRecord = userRecord;
        return user;
    }

    public static User getUser(DocumentReference reference) throws ExecutionException, InterruptedException, ParseException, FirebaseAuthException {
        return getUser(reference, null);
    }

    public static void saveUser(User user, String password) throws ExecutionException, InterruptedException, FirebaseAuthException {
        if (user.firebaseRecord == null) {
            // handle new user
            if (password == null) {
                password = RandomGenerator.generatePassayPassword();
            }
            user.firebaseRecord = Auth.newUserWithEmailAndPassword(user.email, password, user.name);
        }
        ApiFuture<WriteResult> future = getCollection().document(user.firebaseRecord.getUid()).set(user.encodeJSON().toMap());
        System.out.println("Update time : " + future.get().getUpdateTime());
    }
}
