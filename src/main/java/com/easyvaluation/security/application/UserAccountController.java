package com.easyvaluation.security.application;

import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountRepository;
import com.easyvaluation.security.domain.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserAccountController {

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    UserAccountService userAccountService;

    @GetMapping("/user-accounts")
    public ResponseEntity<List<UserAccount>> getMany() {
        return ResponseEntity.ok(userAccountRepository.findAll());
    }

    @GetMapping("/admin")
    public ResponseEntity<String> getAdminPanel() {
        return ResponseEntity.ok("Admin panel");
    }

    @PostMapping("/user-accounts/register")
    public ResponseEntity<UserAccount> save(@RequestBody UserAccount userAccount){
        try {
            UserAccount savedUser = userAccountService.save(userAccount);
            return ResponseEntity.ok(savedUser);
        }catch(DataIntegrityViolationException e){
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.WARNING, "Login or email already in use");
            return ResponseEntity.internalServerError()
                    .headers(responseHeaders)
                    .body(userAccount);
        }catch (Exception e){
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(HttpHeaders.WARNING, "Some error occured");
            return ResponseEntity.internalServerError()
                    .headers(responseHeaders)
                    .body(userAccount);
        }
    }
};
