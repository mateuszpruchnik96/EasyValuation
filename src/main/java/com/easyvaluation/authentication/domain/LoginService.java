package com.easyvaluation.authentication.domain;

import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.coyote.ContinueResponseTiming;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LoginService{
    @Autowired
    UserAccountService userAccountService;

    public String login(UserAccount userAccount){
        boolean isExist = userAccountService.isExist(userAccount).getKey();
        if(isExist == true){

        long currentTimeMillis = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(userAccount.getLogin())
                .claim("roles","user")
                .setIssuedAt(new Date(currentTimeMillis))
                .setExpiration(new Date(currentTimeMillis +(50*1000)))
                .signWith(SignatureAlgorithm.HS512, userAccount.getPassword())
                .compact();
        }else{
            return "Wrong login or password!";
        }
    }
}

