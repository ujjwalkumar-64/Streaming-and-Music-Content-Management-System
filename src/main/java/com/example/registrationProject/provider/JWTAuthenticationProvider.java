package com.example.registrationProject.provider;

import com.example.registrationProject.service.imp.UserServiceImpl;
import com.example.registrationProject.token.JwtAuthenticationToken;
import com.example.registrationProject.utility.JWTUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class JWTAuthenticationProvider implements AuthenticationProvider {

    private JWTUtil jwtUtil;
    private UserServiceImpl userServiceImpl;

    public JWTAuthenticationProvider(JWTUtil jwtUtil,UserServiceImpl userServiceImpl) {
        this.jwtUtil = jwtUtil;
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token= ((JwtAuthenticationToken)(authentication)).getToken();
        String username= jwtUtil.validateAndExtractUsername(token);

        if(username==null){
            throw new BadCredentialsException("Invalid token");
        }

        UserDetails user= userServiceImpl.loadUserByUsername(username);

        return  new UsernamePasswordAuthenticationToken(user,token,user.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
