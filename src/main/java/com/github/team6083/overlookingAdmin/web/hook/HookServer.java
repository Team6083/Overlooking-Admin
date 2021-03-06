package com.github.team6083.overlookingAdmin.web.hook;

import com.github.team6083.overlookingAdmin.firebase.Auth;
import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.github.team6083.overlookingAdmin.module.User;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import fi.iki.elonen.NanoHTTPD;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class HookServer {

    public static final String hookURL = "/hook";
    public static boolean init = false;

    public NanoHTTPD.Response handle(NanoHTTPD.IHTTPSession session) throws IOException, NanoHTTPD.ResponseException, NoSuchMethodException {
        if (!init) {
            HookRouter.init();
            init = true;
        }

        NanoHTTPD.Response r;
        String uri = session.getUri();
        uri = uri.substring(hookURL.length());

        // route handlers
        HookHandler handler = HookRouter.getHandler(uri);

        if (handler != null) {
            HashMap<String, String> bodyMap = new HashMap<>();
            String body = "";
            if (session.getMethod().equals(NanoHTTPD.Method.POST)) {
                session.parseBody(bodyMap);
                body = bodyMap.get("postData");
            }
            System.out.println("Handling hook '" + uri + "' with body: " + body);

            uri = uri.substring(HookRouter.getAppUri(uri).length());
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
