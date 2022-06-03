package com.easyvaluation.authentication.domain;

public class SecretKeyConfig {
    private static final String SECRET_KEY = "lokiloki";
    private static final String SECRET_REFRESH_KEY = "asdfasdfasdf";


    public static String getSECRET_KEY() {
        return SECRET_KEY;
    }
    public static String getSECRET_REFRESH_KEY() {
        return SECRET_REFRESH_KEY;
    }

}
