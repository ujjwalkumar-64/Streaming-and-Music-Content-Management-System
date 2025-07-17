package com.example.registrationProject.filter;

import com.example.registrationProject.entity.Status;
import com.example.registrationProject.entity.User;
import com.example.registrationProject.request.LoginRequest;
import com.example.registrationProject.utility.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected  void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException , IOException {

        if(!request.getServletPath().startsWith("/login")){
            filterChain.doFilter(request,response);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        LoginRequest login = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

        UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(login.getEmail(),login.getPassword());


        Authentication authResult= authenticationManager.authenticate(authToken);
        System.out.println(authResult);
        System.out.println(authToken);


        if(authResult.isAuthenticated() ){
            String token= jwtUtil.generateToken(authResult.getName(),15); //15min
            response.setHeader("Authorization","Bearer "+token);

            String refreshToken= jwtUtil.generateToken(authResult.getName(),7*24*60); //7 days
            Cookie refreshCookie= new Cookie("refreshToken",refreshToken);
            refreshCookie.setPath("/refreshToken");
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setMaxAge(7*24*60*60);
            response.addCookie(refreshCookie);
        }

    }
}
