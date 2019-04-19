package com.github.team6083.overlookingAdmin.module;

import com.github.team6083.overlookingAdmin.firebase.db.MemberProfileCollection;
import com.github.team6083.overlookingAdmin.util.UserPermission;
import com.google.firebase.auth.UserRecord;
import org.json.JSONObject;

import java.util.Date;
import java.util.concurrent.ExecutionException;

public class User {
    public UserRecord firebaseRecord;
    public String name;
    public String email;
    public String team;
    public ClassData classData;
    public String phoneNumber;
    public int firstYear;
    public Date birthDay;
    public UserPermission permission;
    public String position;
    public String memberProfileRef;

    public User() {
        permission = UserPermission.NONE;
    }

    public MemberProfile getProfile() throws ExecutionException, InterruptedException {
        if (!memberProfileRef.equals("")) {
            return MemberProfileCollection.getProfile(memberProfileRef);
        } else {
            return null;
        }
    }

    public JSONObject encodeJSON() {
        return encodeJSON(this);
    }

    public JSONObject encodeJSON(boolean includeFirebaseRecord) {
        return encodeJSON(this, includeFirebaseRecord);
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
        json.put("permission", user.permission.ordinal());
        json.put("position", user.permission);
        json.put("memberProfileRef", user.memberProfileRef);
        return json;
    }

    public static JSONObject encodeJSON(User user, boolean includeFirebaseRecord) {
        JSONObject jsonObject = encodeJSON(user);

        if (includeFirebaseRecord) {
            jsonObject.put("uid", user.firebaseRecord.getUid());
            jsonObject.put("photoUrl", user.firebaseRecord.getPhotoUrl());
            jsonObject.put("disabled", user.firebaseRecord.isDisabled());
            jsonObject.put("verified", user.firebaseRecord.isEmailVerified());
        }

        return jsonObject;
    }

    public static User decodeJSON(String jsonString) {
        return decodeJSON(new JSONObject(jsonString));
    }

    public static User decodeJSON(String jsonString, UserRecord userRecord) {
        return decodeJSON(new JSONObject(jsonString), userRecord);
    }

    public static User decodeJSON(JSONObject json) {
        return decodeJSON(json, null);
    }

    public static User decodeJSON(JSONObject json, UserRecord userRecord) {
        User user = new User();
        user.birthDay = (Date) json.get("birthDay");
        user.classData = ClassData.decodeJSON(json.getString("classDataString"));
        user.email = json.getString("email");
        user.firstYear = json.getInt("firstYear");
        user.name = json.getString("name");
        user.phoneNumber = json.getString("phoneNumber");
        user.team = json.getString("team");
        user.permission = UserPermission.convert(json.getInt("permission"));
        user.firebaseRecord = userRecord;
        user.position = json.getString("position");
        user.memberProfileRef = json.getString("memberProfileRef");

        return user;
    }

    public boolean checkPermission(UserPermission permission) {
        return checkPermission(permission, this);
    }

    public static boolean checkPermission(UserPermission permission, User user) {
        if (user.permission.ordinal() >= permission.ordinal()) {
            return true;
        } else {
            return false;
        }
    }
}
