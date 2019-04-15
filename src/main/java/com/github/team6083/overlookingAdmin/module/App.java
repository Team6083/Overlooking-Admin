package com.github.team6083.overlookingAdmin.module;

import com.google.cloud.firestore.DocumentReference;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

public class App {
    public String name;
    public String app_token;
    public DocumentReference app_Doc;

    public JSONObject encodeJSON(){
        return encodeJSON(this);
    }

    public JSONObject encodeJSON(boolean includeDoc){
        return encodeJSON(this, includeDoc);
    }

    public static JSONObject encodeJSON(App app){
        return encodeJSON(app, false);
    }

    public static JSONObject encodeJSON(App app, boolean includeDoc){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", app.name);
        jsonObject.put("app_token", app.app_token);
        if(includeDoc){
            if(app.app_Doc != null){
                jsonObject.put("docId", app.app_Doc.getId());
            } else{
                jsonObject.put("docId", "undefined");
            }
        }

        return jsonObject;
    }

    public static JSONArray encodeAppsJSON(List<App> list){
        JSONArray jsonArray = new JSONArray();

        Iterator it = list.iterator();

        while (it.hasNext()){
            App app = (App) it.next();

            jsonArray.put(app.encodeJSON(true));
        }

        return jsonArray;
    }

    public static App decodeJSON(JSONObject jsonObject, DocumentReference documentReference){
        App app = new App();
        app.name = jsonObject.getString("name");
        app.app_token = jsonObject.getString("app_token");
        app.app_Doc = documentReference;
        return app;
    }
}