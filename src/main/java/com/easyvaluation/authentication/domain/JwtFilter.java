package com.easyvaluation.authentication.domain;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends BasicAuthenticationFilter {

    TokenProvider tokenProvider = new TokenProvider();

    public JwtFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException, ServletException {

        String header = httpServletRequest.getHeader("Authorization");
        String endpoint =httpServletRequest.getServletPath();
        if( !endpoint.endsWith("/login") && !endpoint.endsWith("/register")){
            UsernamePasswordAuthenticationToken authResult = tokenProvider.getAuthenticationByToken(header);
            SecurityContextHolder.getContext().setAuthentication(authResult);
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
