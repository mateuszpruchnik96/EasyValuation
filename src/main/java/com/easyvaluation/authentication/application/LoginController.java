package com.easyvaluation.authentication.application;

import com.easyvaluation.authentication.domain.LoginService;
import com.easyvaluation.security.domain.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {

    LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserAccount user, HttpServletResponse response){
        try {
            String jwtToken = loginService.login(user);
            return new ResponseEntity<>(jwtToken, HttpStatus.OK);
        } catch(EntityNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
//        response.addCookie(new Cookie("Secure-Fgp", loginService.generateFingerprint()));
//        response.addHeader("Set-Cookie", loginService.generateFingerprint());
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<String> refreshToken(@RequestBody String refreshToken, HttpServletRequest request, HttpServletResponse response){
        try {

            String newTokens = loginService.generateNewTokensAfterRefresh(refreshToken);

            return new ResponseEntity<>(newTokens, HttpStatus.OK);
        } catch (EntityNotFoundException exception) {
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
