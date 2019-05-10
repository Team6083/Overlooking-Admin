package com.github.team6083.overlookingAdmin.web.hook;

import com.github.team6083.overlookingAdmin.firebase.Auth;
import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.github.team6083.overlookingAdmin.module.User;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.handler.AppsHandler;
import com.github.team6083.overlookingAdmin.web.hook.handler.FieldConfigHandler;
import com.github.team6083.overlookingAdmin.web.hook.handler.MemberProfileHandler;
import com.github.team6083.overlookingAdmin.web.hook.handler.UsersHandler;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HookRouter {
    public NanoHTTPD.Response handle(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException {
        NanoHTTPD.Response r = null;
        String uri = session.getUri();
        uri = uri.substring(5);

        HookHandler handler = null;

        // route handlers
        if (uri.contains("/users/")) {
            handler = new UsersHandler();
        } else if (uri.contains("/AppsHandler/")) {
            handler = new AppsHandler();
        } else if (uri.contains("/MemberProfiles/")) {
            handler = new MemberProfileHandler();
        } else if (uri.contains("/FieldConfigHandler/")) {
            handler = new FieldConfigHandler();
        }

        if (handler != null) {
            String body = "";
            if (session.getMethod().equals(NanoHTTPD.Method.POST)) {
                session.parseBody(new HashMap<>());
                body = session.getQueryParameterString();
            }
            r = handler.handle(uri, session.getHeaders(), body, session.getMethod());
        } else {
            r = NanoHTTPD.newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, NanoHTTPD.MIME_PLAINTEXT, "404 not found");
            System.err.println("ERROR: handler is null");
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
