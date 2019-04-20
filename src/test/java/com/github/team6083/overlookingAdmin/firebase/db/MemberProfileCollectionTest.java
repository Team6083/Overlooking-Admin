package com.github.team6083.overlookingAdmin.firebase.db;

import com.github.team6083.overlookingAdmin.firebase.FirebaseMain;
import com.github.team6083.overlookingAdmin.firebase.ServiceAcc;
import com.github.team6083.overlookingAdmin.module.ClassData;
import com.github.team6083.overlookingAdmin.module.FieldConfig;
import com.github.team6083.overlookingAdmin.module.MemberProfile;
import com.github.team6083.overlookingAdmin.util.types.PhoneNumberData;
import com.google.cloud.firestore.DocumentReference;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class MemberProfileCollectionTest {

    @BeforeAll
    static void beforeTest() throws IOException {
        FirebaseMain.init(ServiceAcc.getServiceAccount(), "https://overlooking-admin.firebaseio.com");
    }

    @Test
    void saveProfile() throws ExecutionException, InterruptedException {
        FieldConfig fieldConfig = FieldConfigCollection.getConfig("KiEN5e9RjFjuG5Q9caxA");

        MemberProfile memberProfile = new MemberProfile(fieldConfig);

        String name = "name";
        Date dt = new Date();
        LocalDate ld = LocalDate.now();
        PhoneNumberData phoneNumberData = new PhoneNumberData(886, 912345678);
        ClassData cd = new ClassData();
        cd.className = "class1";
        cd.number = 87;
        memberProfile.put("name", name);
        memberProfile.put("dateTime", dt);
        memberProfile.put("date", ld);
        memberProfile.put("phone number", phoneNumberData);
        memberProfile.put("cd", cd);

        DocumentReference ref = MemberProfileCollection.saveProfile(memberProfile);

        MemberProfile mp = MemberProfileCollection.getProfile(ref);

        String gName = (String) mp.get("name");
        assertEquals(name, gName);

        LocalDate gLd = (LocalDate) mp.get("date");
        assertEquals(ld, gLd);

        Date gDt = (Date) mp.get("dateTime");
        assertEquals(dt, gDt);
    }
}