package com.github.team6083.overlookingAdmin.web.hook.handler;

import com.github.team6083.overlookingAdmin.firebase.db.FieldConfigCollection;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.HookHandler;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.List;
import java.util.concurrent.ExecutionException;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class FieldConfigHandler extends HookHandler {
    public FieldConfigHandler() throws NoSuchMethodException {
        super();
    }

    @Override
    protected void setHookMethodMap() throws NoSuchMethodException {
        hookMethodMap.put("/profileList", this.getClass().getMethod("profileList", String.class), UserPermission.LEADER, NanoHTTPD.Method.GET);
    }

    private NanoHTTPD.Response profileList(String body) throws ExecutionException, InterruptedException {
        List<com.github.team6083.overlookingAdmin.module.FieldConfig> list = FieldConfigCollection.getAll();

        JSONArray out = new JSONArray();

        for (com.github.team6083.overlookingAdmin.module.FieldConfig fieldConfig : list) {
            JSONObject object = fieldConfig.encodeJSON();
            object.put("uid", fieldConfig.documentReference.getId());
            out.put(object);
        }

        return newFixedLengthResponse(out.toString());
    }
}
