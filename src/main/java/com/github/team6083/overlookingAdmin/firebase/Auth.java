package com.github.team6083.overlookingAdmin.firebase;

import com.google.firebase.auth.*;

public class Auth {
    public static ListUsersPage getListUsersPage() throws FirebaseAuthException {
        return FirebaseAuth.getInstance().listUsers(null);
    }

    public static UserRecord getUserFromIdToken(String token) throws FirebaseAuthException {
        FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdToken(token, true);
        return getUserByUID(firebaseToken.getUid());
    }

    public static UserRecord newUserWithEmailAndPassword(String email, String password, String name) throws FirebaseAuthException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password).setDisplayName(name);

        UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
        System.out.println("Successfully created new user: " + userRecord.getUid());

        return userRecord;
    }

    public static UserRecord getUserByUID(String uid) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUser(uid);
    }

    public static UserRecord getUserByEmail(String email) throws FirebaseAuthException {
        return FirebaseAuth.getInstance().getUserByEmail(email);
    }
}
