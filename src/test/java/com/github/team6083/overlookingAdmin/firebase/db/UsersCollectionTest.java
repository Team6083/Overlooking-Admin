package com.github.team6083.overlookingAdmin.firebase.db;

import com.github.team6083.overlookingAdmin.firebase.Auth;
import com.github.team6083.overlookingAdmin.firebase.CloudFirestore;
import com.github.team6083.overlookingAdmin.firebase.FirebaseMain;
import com.github.team6083.overlookingAdmin.firebase.ServiceAcc;
import com.github.team6083.overlookingAdmin.module.ClassData;
import com.github.team6083.overlookingAdmin.module.User;
import com.google.cloud.firestore.CollectionReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class UsersCollectionTest extends CloudFirestore {

    @BeforeAll
    static void beforeTest() throws IOException {
        FirebaseMain.init(ServiceAcc.getServiceAccount(), "https://overlooking-admin.firebaseio.com");
    }

    @Test
    void saveUserByUid() throws InterruptedException, ExecutionException, FirebaseAuthException, ParseException {
        User user = new User();

        user.team = "overlooking";
        user.phoneNumber = "0912345678";
        user.name = "test user";
        user.firstYear = 2019;
        user.email = "team6083@team6083.org";
        user.classData = new ClassData();
        user.classData.number = 60;
        user.classData.className = "6083";
        user.birthDay = new Date();
        user.firebaseRecord = null;

        UsersCollection.saveUserByUid(user, "testpass");

        User u = UsersCollection.getUserFromRef(UsersCollection.getUserRefByEmail(user.email));

        assertEquals(user.email, u.email);
        assertEquals(user.firstYear, u.firstYear);
        assertEquals(user.classData.number, u.classData.number);

        FirebaseAuth.getInstance().deleteUser(user.firebaseRecord.getUid());
    }
}