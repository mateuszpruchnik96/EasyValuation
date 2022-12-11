package com.easyvaluation.authentication.domain;

import com.easyvaluation.security.domain.UserAccount;
import com.easyvaluation.security.domain.UserRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;

@MockitoSettings(strictness = Strictness.STRICT_STUBS)
@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    // Will be expired at 2054
    String jwtTokenLong = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiamFuIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTY1OTMzNTYxMCwiZXhwIjoyNjU5MzM2NTEwfQ.1BgrRu9O43fTgPuqFApEMjTDBPZyfos4a8cO09rWeAyGU-SkhnHXlSC7KZK5y_5z99CgT13zeYRY9QtwrcWpaw";

    // Now expired
    String jwtTokenExpired = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJuYW1lIjoiamFuIiwicm9sZSI6IlJPTEVfVVNFUiIsImlhdCI6MTY1OTMzNTYxMCwiZXhwIjoxNjU5MzM1NjExfQ.H53aOUQqNrSOYFtkgnTPJGmzyqkeyhlq90jjikZtpaqAjSdd7yPYmjlnPBlw2K54njjrFmWGGp5DJ6TRheB5IQ";

    UserRole userRole = new UserRole("ROLE_USER");
    UserAccount user = new UserAccount("jan", "lokiloki", userRole);

    //    @Autowired
    SecurityConfig securityConfig = new SecurityConfig();

//    @InjectMocks
    JwtFilter jwtFilter;

    private AuthenticationManager manager;

    @Autowired
    TokenProvider tokenProvider = new TokenProvider();

    @BeforeEach
    public void setUp() {
        SecurityContextHolder.clearContext();
        String jwtToken = tokenProvider.createToken(user);
        UsernamePasswordAuthenticationToken authenticationToken = tokenProvider.getAuthenticationByToken(jwtToken);
        authenticationToken.setDetails(new WebAuthenticationDetails(new MockHttpServletRequest()));

        UsernamePasswordAuthenticationToken authResult =
                tokenProvider.getAuthenticationByToken("Bearer " + jwtToken);
        SecurityContextHolder.getContext().setAuthentication(authResult);

//        Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Collections.singleton(new SimpleGrantedAuthority(user.getRoles().get(0).toString()));
//        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
//                = new UsernamePasswordAuthenticationToken(user.getLogin(), null, simpleGrantedAuthorities);

        Authentication rod = new UsernamePasswordAuthenticationToken("rod", "koala",
                AuthorityUtils.createAuthorityList("ROLE_USER"));
        this.manager = mock(AuthenticationManager.class);
//        given(this.manager.authenticate(authenticationToken)).willReturn(rod);
//        given(this.manager.authenticate(not(eq(authenticationToken)))).willThrow(new BadCredentialsException(""));
        this.jwtFilter = new JwtFilter(this.manager, new BasicAuthenticationEntryPoint());
    }

    @AfterEach
    public void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternalPositiveWhenProperTokenIsInHeader() throws Exception {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwtTokenLong);
        request.setRequestURI("/hello3");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        //when
        jwtFilter.doFilterInternal(request, response , filterChain);

        //then
        assertThat(response.getStatus(), is(HttpStatus.OK.value()));
    }

    @Test
    void doFilterInternalThrow401ErrorWhenExpiredTokenIsInHeader() throws Exception {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + jwtTokenExpired);
        request.setRequestURI("/test");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain filterChain = new MockFilterChain();

        //when
        jwtFilter.doFilterInternal(request, response , filterChain);

        //then
        assertThat(response.getStatus(), is(HttpStatus.UNAUTHORIZED.value()));
    }


}