package com.github.team6083.overlookingAdmin;

import com.github.team6083.overlookingAdmin.firebase.FirebaseMain;
import com.github.team6083.overlookingAdmin.firebase.ServiceAcc;
import com.google.firebase.auth.*;

import java.io.*;

public class OverAdminServer {
    public static void main(String[] args) throws IOException, FirebaseAuthException {
        FirebaseMain.init(ServiceAcc.getServiceAccount(), "https://overlooking-admin.firebaseio.com");

        // Start listing users from the beginning, 1000 at a time.
        ListUsersPage page = FirebaseAuth.getInstance().listUsers(null);
        while (page != null) {
            for (ExportedUserRecord user : page.getValues()) {
                System.out.println("User: " + user.getUid());
            }
            page = page.getNextPage();
        }
    }
}
