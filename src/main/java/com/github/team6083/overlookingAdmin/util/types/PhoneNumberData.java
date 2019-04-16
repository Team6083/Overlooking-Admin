package com.github.team6083.overlookingAdmin.util.types;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import java.util.regex.Pattern;

public class PhoneNumberData {

    public final static PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    private PhoneNumber phoneNumber;

    public PhoneNumberData(int countryCode, long number){
        phoneNumber = new PhoneNumber().setCountryCode(countryCode).setNationalNumber(number);
    }

    public String getRegionCallingNumber(String region){
        return phoneUtil.formatOutOfCountryCallingNumber(this.phoneNumber, region);
    }

    public boolean checkValidate(){
        return checkValidate(this.phoneNumber);
    }

    public static boolean checkValidate(PhoneNumber number){
        return phoneUtil.isValidNumber(number);
    }

    public String getNumber(){
        return getNumber(phoneNumber);
    }

    public String getNumber(PhoneNumberUtil.PhoneNumberFormat format){
        return getNumber(phoneNumber, format);
    }

    public static String getNumber(PhoneNumber phoneNumber){
        return getNumber(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    }

    public static String getNumber(PhoneNumber phoneNumber, PhoneNumberUtil.PhoneNumberFormat format){
        return phoneUtil.format(phoneNumber, format);
    }
}
