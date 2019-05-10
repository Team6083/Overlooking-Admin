package com.github.team6083.overlookingAdmin.web.hook.handler;

import com.github.team6083.overlookingAdmin.firebase.Auth;
import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.github.team6083.overlookingAdmin.module.User;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.HookHandler;
import com.github.team6083.overlookingAdmin.web.hook.HookServer;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;

import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static fi.iki.elonen.NanoHTTPD.*;

import fi.iki.elonen.NanoHTTPD.Response.*;
import org.json.JSONException;
import org.json.JSONObject;

public class UsersHandler extends HookHandler {
    @Override
    public NanoHTTPD.Response handle(String uri, Map<String, String> header, String body, NanoHTTPD.Method method) {
        String idToken = HookServer.getIdToken(header);
        NanoHTTPD.Response r = null;

        if (uri.equals("/users/usersList")) {
            try {
                if (HookServer.checkPermission(idToken, UserPermission.LEADER)) {
                    ListUsersPage page = Auth.getListUsersPage();
                    JSONArray array = new JSONArray();

                    while (page != null) {
                        for (ExportedUserRecord userRecord : page.getValues()) {
                            User user = UsersCollection.getUser(userRecord);
                            JSONObject json;
                            if (user != null) {
                                json = user.encodeJSON(true);
                                array.put(json);
                            }

                        }
                        page = page.getNextPage();
                    }

                    r = newFixedLengthResponse(array.toString());
                } else {
                    r = newFixedLengthResponse(Status.UNAUTHORIZED, NanoHTTPD.MIME_PLAINTEXT, "no permission");
                }
            } catch (InterruptedException | ExecutionException | ParseException e) {
                e.printStackTrace();
            } catch (FirebaseAuthException e) {
                e.printStackTrace();
            }
        } else if (uri.equals("/users/addUser")) {
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(body);
            } catch (JSONException e){
                return badRequest("json invalid");
            }

            if (!jsonObject.has("email") || !jsonObject.has("psw")) {
                return badRequest("email and psw is required");
            }

        }

        return r;
    }
}
