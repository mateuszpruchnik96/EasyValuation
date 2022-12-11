package com.easyvaluation.authentication.domain;

import com.easyvaluation.authentication.domain.refreshToken.RefreshToken;
import com.easyvaluation.authentication.domain.refreshToken.RefreshTokenService;
import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountService;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.EntityNotFoundException;
import java.util.AbstractMap;

import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willCallRealMethod;


@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class LoginServiceTest {
    @InjectMocks
    LoginService loginService;
    @Mock
    UserAccountService userAccountService;
    @Mock
    TokenProvider tokenProvider;
    @Mock
    RefreshTokenService refreshTokenService;

    UserAccount userAccount;

//    @Test
//    void contextLoads(){
//        assertThat(tokenProvider, notNullValue());
//        assertThat(userAccountService, notNullValue());
//    }

    @BeforeEach
    void initializeUser(){
        userAccount = new UserAccount();
        userAccount.setPassword("lokiloki");
        userAccount.setLogin("jan");
        userAccount.setId(1L);
    }

    @AfterEach
    void cleanup(){
        userAccount = null;
    }

    @Test
    void loginMethodShouldReturnStringWithTokensWithProperLogAndPass() throws NoSuchFieldException {
        //given
        given(tokenProvider.createToken(userAccount)).willCallRealMethod();
        given(userAccountService.isExist(userAccount)).willReturn(new AbstractMap.SimpleEntry(true, "Succesful login"));
        given(userAccountService.findByLogin(userAccount)).willReturn(userAccount);
        given(refreshTokenService.createRefreshToken(userAccount)).willReturn(new RefreshToken());

        //when
        String tokensString = loginService.login(userAccount);

        //then
        assertThat(tokensString, StringContains.containsString("Token"));
    }

    @Test
    void loginMethodShouldThrowAnErrorIfLogOrPassAreWrong() {
        //given
        given(userAccountService.isExist(userAccount)).willReturn(new AbstractMap.SimpleEntry(false, "Wrong login or password"));

        //when
        //then
        assertThrows(EntityNotFoundException.class, ()->loginService.login(userAccount));
    }

    @Test
    void loginMethodShouldThrowAnErrorWhenFindByLoginThrowsEntityNotFoundException() {
        //given
        given(userAccountService.isExist(userAccount)).willReturn(new AbstractMap.SimpleEntry(true, ""));
        given(userAccountService.findByLogin(userAccount)).willThrow(EntityNotFoundException.class);

        //when
        //then
        assertThrows(EntityNotFoundException.class, ()->loginService.login(userAccount));
    }

}