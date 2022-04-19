package com.easyvaluation.authentication.domain;

import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService{
    @Autowired
    UserAccountService userAccountService;

    @Autowired
    TokenProvider tokenProvider;

    public String login(UserAccount userAccount){
        boolean isExist = userAccountService.isExist(userAccount).getKey();
        if(isExist == true){
            return tokenProvider.createToken(userAccount);
        }else{
            return "Wrong login or password!";
        }
    }
}

