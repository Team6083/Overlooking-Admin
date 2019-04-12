package com.github.team6083.overlookingAdmin.module;

import com.google.cloud.Timestamp;
import com.google.firebase.auth.UserRecord;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

public class User {
    public UserRecord firebaseRecord;
    public String name;
    public String email;
    public String team;
    public ClassData classData;
    public String phoneNumber;
    public int firstYear;
    public Date birthDay;

    public JSONObject encodeJSON(){
        return encodeJSON(this);
    }

    public static JSONObject encodeJSON(User user) {
        JSONObject json = new JSONObject();
        json.put("name", user.name);
        json.put("email", user.email);
        json.put("team", user.team);
        json.put("phoneNumber", user.phoneNumber);
        json.put("firstYear", user.firstYear);
        json.put("birthDay", user.birthDay);
        json.put("classDataString", user.classData.encodeJSON().toString());
        return json;
    }

    public static User decodeJSON(String jsonString) throws ParseException {
        return decodeJSON(new JSONObject(jsonString));
    }

    public static User decodeJSON(JSONObject json) throws ParseException {
        User user = new User();
        user.birthDay = (Date) json.get("birthDay");
        user.classData = ClassData.decodeJson(json.getString("classDataString"));
        user.email = json.getString("email");
        user.firstYear = json.getInt("firstYear");
        user.name = json.getString("name");
        user.phoneNumber = json.getString("phoneNumber");
        user.team = json.getString("team");
        return user;
    }
}
