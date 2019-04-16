package com.github.team6083.overlookingAdmin.module;

import java.util.Map;

public class FieldConfig {

    enum DataTypes{
        Int,
        Number,
        String,
        Phone,
        Address,
        Email,
        Date,
        DateTime,
        Time,
        Boolean,
        CLASSData
    }

    private String configName;
    private Map<String, DataTypes> fields;

    public void addField(String name, DataTypes type){
        fields.put(name, type);
    }

    public Map<String, DataTypes> getFields(){
        return fields;
    }

    public String getName(){
        return configName;
    }
}
