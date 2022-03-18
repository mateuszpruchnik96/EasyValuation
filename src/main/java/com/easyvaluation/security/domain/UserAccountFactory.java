package com.easyvaluation.security.domain;

import com.easyvaluation.foundations.domain.AbstractFactory;
import org.springframework.stereotype.Component;

@Component
public class UserAccountFactory implements AbstractFactory<UserAccount> {

    @Override
    public UserAccount create() { return new UserAccount(); }
}
