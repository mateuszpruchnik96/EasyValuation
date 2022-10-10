package com.easyvaluation.authentication.domain.refreshToken;

import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserAccountService;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import javax.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.AbstractMap;
import java.util.UUID;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.BDDMockito.given;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class RefreshTokenServiceTest {
    @InjectMocks
    RefreshTokenService refreshTokenService;
    @Mock
    RefreshTokenRepository refreshTokenRepository;
    @Mock
    UserAccountService userAccountService;

    UserAccount userAccount;

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
    void createRefreshTokenShouldReturnProperRefreshTokenObjectForProperLogAndPass(){
        //given


        given(userAccountService.findById(userAccount.getId())).willReturn(userAccount);

        //when
        RefreshToken token = refreshTokenService.createRefreshToken(userAccount);

        //then
        assertThat(token.getToken(), notNullValue());
        assertThat(token.getUserAccount(), equalTo(userAccount));
    }

    @Test
    void createRefreshTokenShouldThrowErrorForWrongLogOrPass(){
        //given
        given(userAccountService.findById(userAccount.getId())).willThrow(EntityNotFoundException.class);

        //when
        //then
        assertThrows(EntityNotFoundException.class, ()-> refreshTokenService.createRefreshToken(userAccount));

    }



}