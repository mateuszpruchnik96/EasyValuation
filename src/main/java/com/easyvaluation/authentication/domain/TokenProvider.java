package com.easyvaluation.authentication.domain;

import com.easyvaluation.security.domain.UserAccount;
import com.fasterxml.jackson.core.JsonParser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;


import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Component
public class TokenProvider {


    public String createToken(UserAccount userAccount){
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("name",userAccount.getLogin())
                .claim("role","ROLE_" + userAccount.getUserType())
                .claim("userId", userAccount.getId())
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis +(15*60*1000))) //15 minutes
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(SecretKeyConfig.getSECRET_KEY()))
                .compact();
    }

    public String createTokenUsingRefreshToken(String refreshToken){
        String[] chunks = refreshToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        try {
            JSONObject jsonObject = new JSONObject("{\"phonetype\":\"N95\",\"cat\":\"WP\"}");
        }catch (JSONException err){
            System.out.println("Error"+ err.toString());
        }

        String payload = new String(decoder.decode(chunks[1]));

        long currentTimeMillis = System.currentTimeMillis();

        return header + payload;
    }

    public String createRefreshToken(UserAccount userAccount){
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("name",userAccount.getLogin())
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis +(7*24*60*60*1000))) //7 days
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(SecretKeyConfig.getSECRET_REFRESH_KEY()))
                .compact();
    }

    public UsernamePasswordAuthenticationToken getAuthenticationByToken(String header) {

        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(TextCodec.BASE64.encode(SecretKeyConfig.getSECRET_KEY()))
                .parseClaimsJws(header.replace("Bearer ", ""));

        String username = claimsJws.getBody().get("name").toString();
        String role = claimsJws.getBody().get("role").toString();
//        String expirationString =claimsJws.getBody().get("exp").toString();
//        LocalDateTime expiration = LocalDateTime.parse(expirationString, DateTimeFormatter.BASIC_ISO_DATE);
//        LocalDateTime now = LocalDateTime.now();
//        Boolean isExpired = now.isAfter(expiration);


        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(role));

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
        return usernamePasswordAuthenticationToken;
    }

    public Long userIdDecoder(String token){
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        if (token == null) {
            return null;
        } else {
            try{
                JSONObject jsonPayload = new JSONObject(payload);
                System.out.println(jsonPayload.get("userId"));
                Long userId = Long.valueOf(jsonPayload.get("userId").toString());
                System.out.println(userId);
                return userId;
            } catch (JSONException e){
                throw new JSONException(e);
            }
        }
    }

//    public UsernamePasswordAuthenticationToken getAuthenticationByRefreshToken(String header) {
//
//        Jws<Claims> claimsJws = Jwts.parser()
//                .setSigningKey(TextCodec.BASE64.encode(SecretKeyConfig.getSECRET_KEY()))
//                .parseClaimsJws(header.replace("Bearer ", ""));
//
//        String username = claimsJws.getBody().get("name").toString();
//        String role = claimsJws.getBody().get("role").toString();
//
//        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(role));
//
//       UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
//                = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
//        return usernamePasswordAuthenticationToken;
//    }
}
