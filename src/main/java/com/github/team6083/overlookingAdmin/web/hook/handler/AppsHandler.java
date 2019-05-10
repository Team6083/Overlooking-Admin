package com.github.team6083.overlookingAdmin.web.hook.handler;

import com.github.team6083.overlookingAdmin.firebase.db.AppsCollection;
import com.github.team6083.overlookingAdmin.module.App;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.HookHandler;
import com.github.team6083.overlookingAdmin.web.hook.HookServer;
import com.google.firebase.auth.FirebaseAuthException;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class AppsHandler extends HookHandler {
    @Override
    public NanoHTTPD.Response handle(String uri, Map<String, String> header, String body, NanoHTTPD.Method method) {
        String idToken = HookServer.getIdToken(header);
        NanoHTTPD.Response r = null;

        if (uri.equals("/AppsHandler/appsList")) {
            try {
                if (HookServer.checkPermission(idToken, UserPermission.ADMIN)) {
                    JSONArray array = App.encodeAppsJSON(AppsCollection.getAllApps());

                    r = newFixedLengthResponse(array.toString());
                } else {
                    r = newFixedLengthResponse(NanoHTTPD.Response.Status.UNAUTHORIZED, NanoHTTPD.MIME_PLAINTEXT, "no permission");
                }
            } catch (InterruptedException | ExecutionException | ParseException e) {
                e.printStackTrace();
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
        }

        return r;
    }
}
