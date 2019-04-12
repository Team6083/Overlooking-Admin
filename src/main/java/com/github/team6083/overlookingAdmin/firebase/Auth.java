package com.github.team6083.overlookingAdmin.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.auth.UserRecord;

public class Auth {
    public static ListUsersPage getListUsersPage() throws FirebaseAuthException {
        return FirebaseAuth.getInstance().listUsers(null);
    }

    public static UserRecord newUserWithEmailAndPassword(String email, String password, String name) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password).setDisplayName(name);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Successfully created new user: " + userRecord.getUid());

        return userRecord;
    }
}
