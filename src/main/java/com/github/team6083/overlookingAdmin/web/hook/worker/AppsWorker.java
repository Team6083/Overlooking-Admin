package com.github.team6083.overlookingAdmin.web.hook.worker;

import com.github.team6083.overlookingAdmin.firebase.Auth;
import com.github.team6083.overlookingAdmin.firebase.db.AppsCollection;
import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.github.team6083.overlookingAdmin.module.App;
import com.github.team6083.overlookingAdmin.module.User;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.HookHandler;
import com.github.team6083.overlookingAdmin.web.hook.HookWorker;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class AppsWorker implements HookWorker {
    @Override
    public NanoHTTPD.Response serve(String uri, Map<String, String> header, String body, NanoHTTPD.Method method) {
        String idToken = HookHandler.getIdToken(header);
        NanoHTTPD.Response r = null;

        if (uri.equals("/Apps/appsList")) {
            try {
                if (HookHandler.checkPermission(idToken, UserPermission.ADMIN)) {
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
