package com.easyvaluation.authentication.domain;

import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountService;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.AbstractMap;

import static org.hamcrest.MatcherAssert.*;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;


@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class LoginServiceTest {
    @InjectMocks
    LoginService loginService;
    @Mock
    UserAccountService userAccountService;
    @Mock
    TokenProvider tokenProvider;

//    @Test
//    void contextLoads(){
//        assertThat(tokenProvider, notNullValue());
//        assertThat(userAccountService, notNullValue());
//    }

    @Test
    void loginMethodShouldReturnStringWithTokens(){
        //given
        UserAccount userAccount = new UserAccount();
        userAccount.setPassword("lokiloki");
        userAccount.setLogin("jan");
        given(tokenProvider.createToken(userAccount)).willCallRealMethod();
        given(tokenProvider.createRefreshToken(userAccount)).willCallRealMethod();
        given(userAccountService.isExist(userAccount)).willReturn(new AbstractMap.SimpleEntry(true, "Succesful login"));

        //when
        String tokensString = loginService.login(userAccount);


        //then
        assertThat(tokensString, StringContains.containsString("Token"));
    }

}