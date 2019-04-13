package com.github.team6083.overlookingAdmin;

import com.github.team6083.overlookingAdmin.firebase.FirebaseMain;
import com.github.team6083.overlookingAdmin.firebase.ServiceAcc;
import com.github.team6083.overlookingAdmin.web.WebServer;
import com.google.firebase.auth.*;

import java.io.*;

public class OverAdminServer {
    public static void main(String[] args) throws IOException, FirebaseAuthException {
        FirebaseMain.init(ServiceAcc.getServiceAccount(), "https://overlooking-admin.firebaseio.com");

        WebServer webServer = new WebServer(8000);

        while (true){

        }
    }
}
