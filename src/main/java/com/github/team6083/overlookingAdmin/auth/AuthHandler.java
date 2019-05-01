package com.github.team6083.overlookingAdmin.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.KeyPair;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AuthHandler {

    private static KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.ES256);
    private static final String issuer = "Overlooking oAuth Server";

    public void handler() {

    }

    private String getJWTString(String aud, String sub, String payload, UUID uuid) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        return Jwts.builder()
                .setAudience(aud)
                .setIssuedAt(new Date())
                .setIssuer(issuer)
                .setSubject(sub)
                .setNotBefore(new Date())
                .setExpiration(calendar.getTime())
                .setId(uuid.toString())
                .signWith(keyPair.getPrivate()).compact();
    }
}
