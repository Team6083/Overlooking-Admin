package com.github.team6083.overlookingAdmin.module;

import org.json.JSONObject;

public class ClassData {
    public String className;
    public int number;

    public JSONObject encodeJSON() {
        return encodeJSON(this);
    }

    public static JSONObject encodeJSON(ClassData cd) {
        JSONObject json = new JSONObject();

        json.put("className", cd.className);
        json.put("number", cd.number);

        return json;
    }

    public static ClassData decodeJSON(String jsonString) {
        return decodeJSON(new JSONObject(jsonString));
    }

    public static ClassData decodeJSON(JSONObject json) {
        ClassData cd = new ClassData();
        cd.className = json.getString("className");
        cd.number = json.getInt("number");
        return cd;
    }
}
