package com.easyvaluation.authentication.domain;

import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;

@Service
public class LoginService{
    @Autowired
    UserAccountService userAccountService;

    @Autowired
    TokenProvider tokenProvider;

    // Random data generator
    SecureRandom secureRandom = new SecureRandom();

    public String login(UserAccount userAccount) throws EntityNotFoundException{
        boolean isExist = userAccountService.isExist(userAccount).getKey();
        if(isExist == true){
//            Cookie jwtTokenCookie = new Cookie("easyCaluationToken", tokenProvider.createToken(userAccount));
//            jwtTokenCookie.setMaxAge(60*24*2); //Max age: seconds
//            jwtTokenCookie.setHttpOnly(true);
//            jwtTokenCookie.setSecure(false); //To change after including https
//            jwtTokenCookie.setPath("/login");
            return "{\"easyValuationToken\": \"" + tokenProvider.createToken(userAccount) + "\", " +
            "\"easyValuationRefreshToken\": \"" + this.generateRefreshToken(userAccount) + "\"}";
//            return jwtTokenCookie;
        }else{
            throw new EntityNotFoundException();
        }
    }

    public String generateFingerprint(){
        //Generate a random string that will constitute the fingerprint for this user
        byte[] randomFgp = new byte[50];
        secureRandom.nextBytes(randomFgp);
        String userFingerprint = DatatypeConverter.printHexBinary(randomFgp);

        //Add the fingerprint in a hardened cookie - Add cookie manually because
        //SameSite attribute is not supported by javax.servlet.http.Cookie class
        String fingerprintCookie = "__Secure-Fgp=" + userFingerprint
                + "; SameSite=Strict; HttpOnly; Secure";
        return  fingerprintCookie;
    }

    public String generateRefreshToken(UserAccount userAccount){
        String newRefreshToken = tokenProvider.createRefreshToken(userAccount);
        userAccountService.setRefreshToken(userAccount, newRefreshToken);
        return newRefreshToken;
    }
}

