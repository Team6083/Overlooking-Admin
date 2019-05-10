package com.github.team6083.overlookingAdmin.web.hook.handler;

import com.github.team6083.overlookingAdmin.firebase.db.AppsCollection;
import com.github.team6083.overlookingAdmin.module.App;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.HookHandler;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;

import java.util.concurrent.ExecutionException;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

public class AppsHandler extends HookHandler {

    public AppsHandler() throws NoSuchMethodException {
        super();
    }

    @Override
    protected void setHookMethodMap() throws NoSuchMethodException {
        hookMethodMap.put("/appsList", this.getClass().getMethod("appsList", String.class), UserPermission.ADMIN, NanoHTTPD.Method.GET);
    }

    public NanoHTTPD.Response appsList(String body) throws ExecutionException, InterruptedException {
        JSONArray array = App.encodeAppsJSON(AppsCollection.getAllApps());

        return newFixedLengthResponse(array.toString());
    }
}
