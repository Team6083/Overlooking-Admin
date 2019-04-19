package com.github.team6083.overlookingAdmin.module;

import com.google.cloud.firestore.DocumentReference;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

public class FieldConfig {

    enum DataTypes {
        Int,
        Number,
        String,
        Phone,
        Email,
        Date,
        DateTime,
        Boolean,
        ClassData
    }

    private String configName;
    private Map<String, DataTypes> fields;
    public DocumentReference docRef;

    public void addField(String name, DataTypes type) {
        fields.put(name, type);
    }

    public void removeField(String name) {
        fields.remove(name);
    }

    public Map<String, DataTypes> getFields() {
        return fields;
    }

    public String getName() {
        return configName;
    }


    public DataTypes getType(String name) {
        return fields.get(name);
    }

    public boolean checkExist(String name) {
        return fields.containsKey(name);
    }

    public static JSONObject encodeJSON(FieldConfig fieldConfig) {
        JSONObject jsonObject = new JSONObject(fieldConfig.fields);

        jsonObject.put("config_name", fieldConfig.configName);

        return jsonObject;
    }

    public static FieldConfig decodeJSON(JSONObject jsonObject, DocumentReference docRef) {
        FieldConfig fieldConfig = new FieldConfig();
        fieldConfig.docRef = docRef;
        fieldConfig.configName = jsonObject.getString("config_name");

        Set<String> keySet = jsonObject.toMap().keySet();

        for (String key : keySet) {
            DataTypes type = fieldConfig.getType(jsonObject.getString(key));
            fieldConfig.addField(key, type);
        }

        return fieldConfig;
    }
}
