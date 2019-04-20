package com.github.team6083.overlookingAdmin.module;

import com.github.team6083.overlookingAdmin.firebase.db.FieldConfigCollection;
import com.github.team6083.overlookingAdmin.util.Converter;
import com.github.team6083.overlookingAdmin.util.types.PhoneNumberData;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MemberProfile {
    public Map<String, Object> profileData;
    private FieldConfig fields;
    public DocumentReference documentReference;

    public MemberProfile(FieldConfig fieldConfig) {
        profileData = new HashMap<>();
        fields = fieldConfig;
    }

    public void put(String key, Object obj) {
        profileData.put(key, obj);
    }

    public Object get(String key) {
        return profileData.get(key);
    }

    public FieldConfig getFields() {
        return fields;
    }

    public JSONObject encodeJSON() {
        return encodeJSON(this);
    }

    public static JSONObject encodeJSON(MemberProfile profile) {
        JSONObject jsonObject = new JSONObject();

        for (String fieldName : profile.fields.getFields().keySet()) {
            FieldConfig.DataTypes type = profile.fields.getType(fieldName);

            switch (type) {
                case Int:
                    jsonObject.put(fieldName, (int) profile.profileData.get(fieldName));
                    break;
                case Number:
                    jsonObject.put(fieldName, (double) profile.profileData.get(fieldName));
                    break;
                case Email:
                case String:
                case DateTime:
                    jsonObject.put(fieldName, profile.profileData.get(fieldName));
                    break;
                case Date:
                    LocalDate localDate = (LocalDate) profile.profileData.get(fieldName);
                    jsonObject.put(fieldName, localDate.toString());
                    break;
                case Phone:
                    PhoneNumberData phoneNumberData = (PhoneNumberData) profile.profileData.get(fieldName);
                    jsonObject.put(fieldName, phoneNumberData.getNumber());
                    break;
                case ClassData:
                    ClassData classData = (ClassData) profile.profileData.get(fieldName);
                    jsonObject.put(fieldName, classData.encodeJSON());
                    break;
                case Boolean:
                    jsonObject.put(fieldName, (boolean) profile.profileData.get(fieldName));
                    break;
            }
        }

        JSONObject out = new JSONObject();

        out.put("data", jsonObject);

        if (profile.fields.documentReference != null) {
            out.put("config", profile.fields.documentReference);
        } else {
            out.put("config", JSONObject.NULL);
        }


        return out;
    }

    public static MemberProfile decodeJSON(JSONObject jsonObject) throws ExecutionException, InterruptedException {
        FieldConfig config = FieldConfigCollection.getConfig((DocumentReference) jsonObject.get("config"));
        MemberProfile memberProfile = new MemberProfile(config);

        JSONObject jsonData = jsonObject.getJSONObject("data");

        for (String name : memberProfile.fields.getFields().keySet()) {
            FieldConfig.DataTypes type = memberProfile.fields.getType(name);

            switch (type) {
                case Phone:
                    PhoneNumberData phoneNumberData = new PhoneNumberData(jsonData.getString(name));
                    memberProfile.put(name, phoneNumberData);
                    break;
                case Date:
                    LocalDate localDate = LocalDate.parse(jsonData.getString(name));
                    memberProfile.put(name, localDate);
                    break;
                case DateTime:
                    Timestamp timestamp = Timestamp.ofTimeSecondsAndNanos(jsonData.getJSONObject(name).getInt("seconds"), jsonData.getJSONObject(name).getInt("nanos"));
                    Date date = Converter.googleTimestampToDate(timestamp);
                    memberProfile.put(name, date);
                    break;
                case ClassData:
                    ClassData classData = ClassData.decodeJSON(jsonData.getJSONObject(name));
                    memberProfile.put(name, classData);
                    break;
                default:
                    memberProfile.put(name, jsonData.get(name));
                    break;
            }
        }

        return memberProfile;
    }

}
