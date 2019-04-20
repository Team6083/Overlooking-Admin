package com.github.team6083.overlookingAdmin.firebase.db;

import com.github.team6083.overlookingAdmin.firebase.FirebaseMain;
import com.github.team6083.overlookingAdmin.firebase.ServiceAcc;
import com.github.team6083.overlookingAdmin.module.FieldConfig;
import com.google.cloud.firestore.DocumentReference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class FieldConfigCollectionTest {

    @BeforeAll
    static void beforeTest() throws IOException {
        FirebaseMain.init(ServiceAcc.getServiceAccount(), "https://overlooking-admin.firebaseio.com");
    }

    @Test
    void saveConfig() throws ExecutionException, InterruptedException {
        FieldConfig fieldConfig = new FieldConfig();
        fieldConfig.addField("name", FieldConfig.DataTypes.String);
        fieldConfig.addField("dateTime", FieldConfig.DataTypes.DateTime);
        fieldConfig.addField("date", FieldConfig.DataTypes.Date);
        fieldConfig.addField("phone number", FieldConfig.DataTypes.Phone);
        fieldConfig.addField("cd", FieldConfig.DataTypes.ClassData);
        fieldConfig.configName = "testConfig";

        DocumentReference ref = FieldConfigCollection.saveConfig(fieldConfig);

        FieldConfig fc = FieldConfigCollection.getConfig(ref);

        assert fc != null;
        assertEquals(fieldConfig.configName, fc.configName);

        assertEquals(fieldConfig.getType("name"), fc.getType("name"));
    }
}