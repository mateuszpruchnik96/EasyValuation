package com.easyvaluation.authentication.application;

import com.easyvaluation.authentication.domain.LoginService;
import com.easyvaluation.security.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping("/login")
    public String login(@RequestBody UserAccount user){
       return loginService.login(user);
    }
}
