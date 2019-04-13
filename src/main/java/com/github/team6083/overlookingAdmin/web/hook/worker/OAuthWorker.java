package com.github.team6083.overlookingAdmin.web.hook.worker;

import com.github.team6083.overlookingAdmin.firebase.Auth;
import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.github.team6083.overlookingAdmin.module.User;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.HookHandler;
import com.github.team6083.overlookingAdmin.web.hook.HookWorker;
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
import org.json.JSONObject;

public class OAuthWorker implements HookWorker {
    @Override
    public NanoHTTPD.Response serve(String uri, Map<String, String> header, String body, NanoHTTPD.Method method) {
        String idToken = HookHandler.getIdToken(header);
        NanoHTTPD.Response r = null;

        if (uri.equals("/OAuth/usersList")) {
            try {
                if (HookHandler.checkPermission(idToken, UserPermission.LEADER)) {
                    ListUsersPage page = Auth.getListUsersPage();
                    JSONArray array = new JSONArray();

                    while (page != null) {
                        for (ExportedUserRecord userRecord : page.getValues()) {
                            User user = UsersCollection.getUser(userRecord);
                            JSONObject json;
                            if(user != null){
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
        }

        return r;
    }
}
