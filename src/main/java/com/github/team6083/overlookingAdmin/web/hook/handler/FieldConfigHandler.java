package com.github.team6083.overlookingAdmin.web.hook.handler;

import com.github.team6083.overlookingAdmin.firebase.db.FieldConfigCollection;
import com.github.team6083.overlookingAdmin.module.FieldConfig;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.HookHandler;
import com.github.team6083.overlookingAdmin.web.hook.HookRouter;
import com.google.firebase.auth.FirebaseAuthException;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class FieldConfigHandler extends HookHandler {
    @Override
    public NanoHTTPD.Response handle(String uri, Map<String, String> header, String body, NanoHTTPD.Method method) {
        String idToken = HookRouter.getIdToken(header);
        NanoHTTPD.Response r = null;


        if (uri.equals("/profileList")) {
            try {
                if (HookRouter.checkPermission(idToken, UserPermission.LEADER)) {
                    List<com.github.team6083.overlookingAdmin.module.FieldConfig> list = FieldConfigCollection.getAll();

                    JSONArray out = new JSONArray();

                    for (com.github.team6083.overlookingAdmin.module.FieldConfig fieldConfig : list) {
                        JSONObject object = fieldConfig.encodeJSON();
                        object.put("uid", fieldConfig.documentReference.getId());
                        out.put(object);
                    }

                    r = newFixedLengthResponse(out.toString());
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
