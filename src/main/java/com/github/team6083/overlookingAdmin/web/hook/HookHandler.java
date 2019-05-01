package com.github.team6083.overlookingAdmin.web.hook;

import com.github.team6083.overlookingAdmin.firebase.Auth;
import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.github.team6083.overlookingAdmin.module.User;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.worker.AppsWorker;
import com.github.team6083.overlookingAdmin.web.hook.worker.FieldConfigWorker;
import com.github.team6083.overlookingAdmin.web.hook.worker.MemberProfilesWorker;
import com.github.team6083.overlookingAdmin.web.hook.worker.UsersWorker;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HookHandler {
    public NanoHTTPD.Response handle(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
        NanoHTTPD.Response r = null;
        String uri = session.getUri();
        uri = uri.substring(5);

        HookWorker worker = null;

        // route handlers
        if (uri.contains("/users/")) {
            worker = new UsersWorker();
        } else if (uri.contains("/Apps/")) {
            worker = new AppsWorker();
        } else if (uri.contains("/MemberProfiles/")) {
            worker = new MemberProfilesWorker();
        } else if (uri.contains("/FieldConfig/")) {
            worker = new FieldConfigWorker();
        }

        if (worker != null) {
            String body = "";
            if (session.getMethod().equals(NanoHTTPD.Method.POST)) {
                session.parseBody(new HashMap<String, String>());
                body = session.getQueryParameterString();
            }
            r = worker.serve(uri, session.getHeaders(), body, session.getMethod());
        } else {
            System.err.println("ERROR: worker is null");
        }

        return r;
    }

    public static boolean checkPermission(String idToken, UserPermission targetPermission) throws InterruptedException, ExecutionException, ParseException, FirebaseAuthException {
        UserRecord userRecord = Auth.getUserFromIdToken(idToken);

        User user = UsersCollection.getUser(userRecord);

        return user.checkPermission(targetPermission);
    }

    public static String getIdToken(Map<String, String> header) {
        return header.get("auth-idtoken");
    }
}
