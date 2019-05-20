package com.github.team6083.overlookingAdmin.web.hook.handler;

import com.github.team6083.overlookingAdmin.firebase.Auth;
import com.github.team6083.overlookingAdmin.firebase.db.UsersCollection;
import com.github.team6083.overlookingAdmin.module.User;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.github.team6083.overlookingAdmin.web.hook.HookHandler;
import com.google.firebase.auth.ExportedUserRecord;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.ListUsersPage;
import fi.iki.elonen.NanoHTTPD;
import org.json.JSONArray;

import java.text.ParseException;
import java.util.concurrent.ExecutionException;

import static fi.iki.elonen.NanoHTTPD.*;

import org.json.JSONException;
import org.json.JSONObject;

public class UsersHandler extends HookHandler {
    public UsersHandler() throws NoSuchMethodException {
        super();
    }

    @Override
    protected void setHookMethodMap() throws NoSuchMethodException {
        hookMethodMap.put("/usersList", UsersHandler.class.getMethod("usersList", String.class), UserPermission.LEADER, NanoHTTPD.Method.GET);
        hookMethodMap.put("/addUser", UsersHandler.class.getMethod("addUser", String.class), UserPermission.LEADER, Method.POST);
    }

    public Response usersList(String body) throws InterruptedException, ExecutionException, FirebaseAuthException, ParseException {
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

        return newFixedLengthResponse(array.toString());
    }

    public Response addUser(String body) throws InterruptedException, ExecutionException, ParseException {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(body);
        } catch (JSONException e) {
            return badRequest("json invalid");
        }

        if (!jsonObject.has("email") || !jsonObject.has("password") || !jsonObject.has("id")) {
            return badRequest("id, email, password is required");
        }

        if(jsonObject.getString("email").equals("") || jsonObject.getString("password").equals("")){
            return badRequest("email, password should not be blank");
        }

        User user;

        if (!jsonObject.getString("id").equals("")) {
            try {
                user = UsersCollection.getUser(Auth.getUserByUID(jsonObject.getString("id")));
            } catch (FirebaseAuthException e) {
                return internalError(e.getMessage());
            }
        } else {
            user = new User();
        }

        user.email = jsonObject.getString("email");

        if (jsonObject.has("name")) {
            user.name = jsonObject.getString("name");
        } else {
            user.name = "unknown";
        }

        if (jsonObject.has("userPermission")) {
            user.permission = UserPermission.valueOf(jsonObject.getString("userPermission"));
        }

        try {
//            UsersCollection.saveUser(user, jsonObject.getString("password"));
        } catch (Exception e) {
            e.printStackTrace();
            return internalError(e.getMessage());
        }

        System.out.println(user.encodeJSON().toString());

        return okResponse("add user success");
    }

}
