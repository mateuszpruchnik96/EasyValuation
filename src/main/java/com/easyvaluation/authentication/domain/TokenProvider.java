package com.easyvaluation.authentication.domain;

import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserRole;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.TextCodec;


//import org.json.JSONException;
//import org.json.JSONObject;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

import java.util.*;

@Component
public class TokenProvider {

    public String createToken(UserAccount userAccount){
        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .claim("name", userAccount.getLogin())
                .claim("role", this.roleParser(userAccount))
                .claim("userId", userAccount.getId())
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis +(15*60*100))) //15 minutes
                .signWith(SignatureAlgorithm.HS512, TextCodec.BASE64.encode(SecretKeyConfig.getSECRET_KEY()))
                .compact();
    }

    public String createTokenUsingRefreshToken(String refreshToken){
        String[] chunks = refreshToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
//        try {
//            JSONObject jsonObject = new JSONObject("{\"phonetype\":\"N95\",\"cat\":\"WP\"}");
//        }catch (JSONException err){
//            System.out.println("Error"+ err.toString());
//        }

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

        String[] roles = role.split(", ");
        List<SimpleGrantedAuthority> roleArrayList = new ArrayList<SimpleGrantedAuthority>();
        for(String roleString : roles)
            roleArrayList.add(new SimpleGrantedAuthority(roleString));
        Set<SimpleGrantedAuthority> set = new HashSet<SimpleGrantedAuthority>(roleArrayList);

        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.synchronizedSet(set);

//                Collections.singleton(new SimpleGrantedAuthority(role));

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
//            try{
//                JSONObject jsonPayload = new JSONObject(payload);
                Map map = new HashMap();
                try {
                    map = new ObjectMapper().readValue(payload, Map.class);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
//                System.out.println(jsonPayload.get("userId"));
//                Long userId = Long.valueOf(jsonPayload.get("userId").toString());
                Long userId = Long.valueOf(map.get("userId").toString());
                System.out.println(userId);
                return userId;
//            } catch (JSONException e){
//                throw new JSONException(e);
//            }
        }
    }

    private String roleParser(UserAccount userAccount){
        String roles = "";
        Collection<UserRole> userRoles = userAccount.getUserRoles();
        for(UserRole userRole : userRoles){
            roles = roles + userRole.getName() + ", ";
        }
        return roles;
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
