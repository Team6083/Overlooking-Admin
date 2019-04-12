package com.github.team6083.overlookingAdmin.firebase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ServiceAcc {
    public static InputStream getServiceAccount(){
        String key = "";
        InputStream serviceAccount = new ByteArrayInputStream(key.getBytes(StandardCharsets.UTF_8));
        return serviceAccount;
    }
}
