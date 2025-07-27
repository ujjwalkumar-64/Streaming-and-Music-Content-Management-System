package com.example.registrationProject.filter;

import com.example.registrationProject.entity.User;
import com.example.registrationProject.token.JwtAuthenticationToken;
import com.example.registrationProject.utility.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.antlr.v4.runtime.Token;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtRefreshFilter extends OncePerRequestFilter {
    private JWTUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    public  JwtRefreshFilter(JWTUtil jwtUtil,AuthenticationManager authenticationManager){
        this.jwtUtil=jwtUtil;
        this.authenticationManager=authenticationManager;
    }

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException
    {
       if(!request.getServletPath().equals("/refreshToken")){
           filterChain.doFilter(request,response);
           return;
       }
       String refreshToken= extractJwtFromRequest(request);
       if(refreshToken == null) {
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           return;
       }

       JwtAuthenticationToken authenticationToken= new JwtAuthenticationToken(refreshToken);
        Authentication authResult = authenticationManager.authenticate(authenticationToken);
        User user = (User) authResult.getPrincipal();

        if(authResult.isAuthenticated()){
            String newToken= jwtUtil.generateToken(authResult.getName(),user,15);  // 15min
            response.setHeader("Authorization","Bearer "+newToken);
        }

    }

    private String extractJwtFromRequest(HttpServletRequest request){

        Cookie[] cookies= request.getCookies();
        if (cookies == null) {
            return null;
        }
        String refreshToken=null;
        for (Cookie cookie : cookies) {
            if("refreshToken".equals(cookie.getName())){
                refreshToken= cookie.getValue();
            }
        }
        return refreshToken;
    }

}
