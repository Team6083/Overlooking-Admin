package com.github.team6083.overlookingAdmin.module;

import com.google.cloud.firestore.DocumentReference;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FieldConfig {

    public enum DataTypes {
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

    public String configName;
    private Map<String, DataTypes> fields;
    public DocumentReference documentReference;

    public FieldConfig(){
        fields = new HashMap<>();
    }

    public void addField(String name, DataTypes type) {
        fields.put(name, type);
    }

    public void removeField(String name) {
        fields.remove(name);
    }

    public Map<String, DataTypes> getFields() {
        return fields;
    }

    public DataTypes getType(String name) {
        return fields.get(name);
    }

    public boolean checkExist(String name) {
        return fields.containsKey(name);
    }

    public JSONObject encodeJSON(){
        return encodeJSON(this);
    }

    public static JSONObject encodeJSON(FieldConfig fieldConfig) {
        JSONObject jsonObject = new JSONObject();

        for(String name: fieldConfig.fields.keySet()) {
            DataTypes dataTypes = fieldConfig.fields.get(name);

            jsonObject.put(name, dataTypes.name());
        }

        JSONObject out = new JSONObject();

        out.put("config", jsonObject);
        out.put("name", fieldConfig.configName);

        return out;
    }

    public static FieldConfig decodeJSON(JSONObject jsonObject, DocumentReference docRef) {
        FieldConfig fieldConfig = new FieldConfig();
        fieldConfig.documentReference = docRef;
        fieldConfig.configName = jsonObject.getString("name");

        JSONObject jsonData = jsonObject.getJSONObject("config");
        Set<String> keySet = jsonData.toMap().keySet();

        for (String key : keySet) {
            DataTypes type = DataTypes.valueOf(jsonData.getString(key));
            fieldConfig.addField(key, type);
        }

        return fieldConfig;
    }
}
