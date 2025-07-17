package com.example.registrationProject.config;

import com.example.registrationProject.entity.Role;
import com.example.registrationProject.entity.User;
import com.example.registrationProject.filter.JWTAuthenticationFilter;
import com.example.registrationProject.filter.JwtRefreshFilter;
import com.example.registrationProject.filter.JwtValidationFilter;
import com.example.registrationProject.provider.JWTAuthenticationProvider;
import com.example.registrationProject.repository.UserRepository;
import com.example.registrationProject.service.imp.UserServiceImpl;
import com.example.registrationProject.utility.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JWTUtil jwtUtil;
    private final UserServiceImpl userServiceImpl;

    @Autowired
    public SecurityConfig(JWTUtil jwtUtil, UserServiceImpl userServiceImpl) {
        this.jwtUtil = jwtUtil;
        this.userServiceImpl = userServiceImpl;
    }

    private static final String[] whiteList = {
            "/register",
            "/login",
            "/otp/validate",
            "/otp/resend"
    };

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userServiceImpl);
        provider.setPasswordEncoder(passwordEncoder());
        return  provider;
    }

    @Bean
    public JWTAuthenticationProvider jwtAuthenticationProvider(){
        return  new JWTAuthenticationProvider(jwtUtil,userServiceImpl) ;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager,
                                                   JWTUtil jwtUtil) throws Exception {

        JWTAuthenticationFilter jwtAuthFilter= new JWTAuthenticationFilter(authenticationManager,jwtUtil);
        JwtValidationFilter jwtValidationFilter= new JwtValidationFilter(authenticationManager);
        JwtRefreshFilter jwtRefreshFilter= new JwtRefreshFilter(jwtUtil,authenticationManager);
        http.authorizeHttpRequests(
                auth-> auth
                        .requestMatchers("/user/deleteAccount").hasAnyAuthority("MANAGE_USER")
                        .requestMatchers("/user/update/permission").hasAnyAuthority("ASSIGN_ROLE")
                        .requestMatchers("/user/update/role").hasAuthority("ASSIGN_ROLE")
                        .requestMatchers("/user/*").hasAnyAuthority("BROWSE_MUSIC")
                        .requestMatchers("/product/delete").hasAnyAuthority("DELETE_CONTENT")
                        .requestMatchers("/product/update").hasAnyAuthority("UPDATE_CONTENT")
                        .requestMatchers("/product/create").hasAnyAuthority("CREATE_CONTENT")
                        .requestMatchers("/product/*").hasAnyAuthority("BROWSE_MUSIC")
                        .requestMatchers(whiteList).permitAll()
                        .anyRequest().authenticated()
        )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidationFilter,JWTAuthenticationFilter.class)
                .addFilterAfter(jwtRefreshFilter, JwtValidationFilter.class)
                .httpBasic(Customizer.withDefaults());

                return http.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(
            DaoAuthenticationProvider daoProvider,
            JWTAuthenticationProvider jwtProvider) {

        return new ProviderManager(Arrays.asList(daoProvider, jwtProvider));
    }






}

