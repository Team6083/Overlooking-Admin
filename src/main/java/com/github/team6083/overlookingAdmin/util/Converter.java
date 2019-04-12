package com.github.team6083.overlookingAdmin.util;

import com.google.cloud.Timestamp;

import java.util.Date;

public class Converter {
    public static Date googleTimestampToDate(Timestamp timestamp){
        return timestamp.toDate();
    }
}
