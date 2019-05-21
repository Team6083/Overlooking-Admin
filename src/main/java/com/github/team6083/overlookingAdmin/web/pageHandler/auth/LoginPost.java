package com.github.team6083.overlookingAdmin.web.pageHandler.auth;

import com.github.team6083.overlookingAdmin.util.router.RouteHandler;
import com.github.team6083.overlookingAdmin.web.WebServer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import fi.iki.elonen.NanoHTTPD;

public class LoginPost implements RouteHandler {
    @Override
    public NanoHTTPD.Response handle(NanoHTTPD.IHTTPSession session) {
        String auth_token = session.getHeaders().get("auth_token");
        String uid = "";
        try {
            uid = FirebaseAuth.getInstance().verifyIdToken(auth_token).getUid();
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }

        String customToken = "";
        try {
            customToken = FirebaseAuth.getInstance().createCustomToken(uid);
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
        }

        NanoHTTPD.Response r = NanoHTTPD.newFixedLengthResponse(customToken);

        r.addHeader("SET-COOKIE", WebServer.getSetAuthCookieHeaderString(customToken));

        return r;
    }
}
