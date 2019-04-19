package com.github.team6083.overlookingAdmin.module;

import com.github.team6083.overlookingAdmin.firebase.db.FieldConfigCollection;
import com.github.team6083.overlookingAdmin.util.types.PhoneNumberData;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MemberProfile {
    private Map<String, Object> profileData;
    private FieldConfig fields;

    public MemberProfile(FieldConfig fieldConfig) {
        profileData = new HashMap<>();
        fields = fieldConfig;
    }

    public void put(String name, Object obj) {
        profileData.put(name, obj);
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
                case Date:
                case DateTime:
                    jsonObject.put(fieldName, profile.profileData.get(fieldName));
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

        jsonObject.put("config", profile.fields.docRef.getId());

        return jsonObject;
    }

    public static MemberProfile decodeJSON(JSONObject jsonObject) throws ExecutionException, InterruptedException {
        FieldConfig config = FieldConfigCollection.getConfig(jsonObject.getString("config"));
        MemberProfile memberProfile = new MemberProfile(config);

        for (String name : memberProfile.fields.getFields().keySet()) {
            FieldConfig.DataTypes type = memberProfile.fields.getType(name);

            switch (type) {
                case Phone:
                    PhoneNumberData phoneNumberData = new PhoneNumberData(jsonObject.getString(name));
                    memberProfile.put(name, phoneNumberData);
                    break;
                case Date:
                    LocalDate localDate = (LocalDate) jsonObject.get(name);
                    memberProfile.put(name, localDate);
                    break;
                case DateTime:
                    Date date = (Date) jsonObject.get(name);
                    memberProfile.put(name, date);
                    break;
                case ClassData:
                    ClassData classData = ClassData.decodeJSON(jsonObject.getString(name));
                    memberProfile.put(name, classData);
                    break;
                default:
                    memberProfile.put(name, jsonObject.get(name));
                    break;
            }

            memberProfile.put(name, jsonObject.get(name));
        }

        return memberProfile;
    }

}
