package com.easyvaluation.authentication.domain;

import com.easyvaluation.security.domain.UserAccount;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis +(15*60*1000))) //15 minutes
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(SecretKeyConfig.getSECRET_KEY()))
                .compact();
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
}
