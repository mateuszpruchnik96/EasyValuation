package com.easyvaluation.authentication.domain;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
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

    public JwtFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint basicAuthenticationEntryPoint) {
        super(authenticationManager, basicAuthenticationEntryPoint);
    }


    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws IOException{

        String header = httpServletRequest.getHeader("Authorization");
        String endpoint = httpServletRequest.getServletPath();
        if( !(endpoint.endsWith("/login") || endpoint.endsWith("/register") || endpoint.endsWith("/refreshtoken") || endpoint.endsWith("/h2-console"))){
            try {
                UsernamePasswordAuthenticationToken authResult;
                if(header!=null){
                    authResult = tokenProvider.getAuthenticationByToken(header);
                    SecurityContextHolder.getContext().setAuthentication(authResult);
                } else {
                    SecurityContextHolder.getContext().setAuthentication(null);
                }

            } catch (ExpiredJwtException e){
                httpServletResponse.sendError(401, "ExpiredJwtException");
            } catch (MalformedJwtException e){
                httpServletResponse.sendError(401, "MalformedJwtException");
            }
        }
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch(ServletException e){
            System.out.println(e);
            System.out.println(httpServletRequest);
            System.out.println(httpServletResponse);
        }
    }
}
