package com.github.team6083.overlookingAdmin.module;

import java.util.HashMap;
import java.util.Map;

public class MemberProfile {
    private Map<String, Object> profileData;
    private FieldConfig fields;

    public MemberProfile(FieldConfig fieldConfig){
        profileData = new HashMap<>();
        fields = fieldConfig;
    }



}
