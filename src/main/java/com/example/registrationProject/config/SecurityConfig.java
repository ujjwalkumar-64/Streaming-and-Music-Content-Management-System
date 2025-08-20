package com.example.registrationProject.config;

import com.example.registrationProject.entity.Role;
import com.example.registrationProject.entity.User;
import com.example.registrationProject.filter.JWTAuthenticationFilter;
import com.example.registrationProject.filter.JwtRefreshFilter;
import com.example.registrationProject.filter.JwtValidationFilter;
import com.example.registrationProject.filter.LogRequestFilter;
import com.example.registrationProject.provider.JWTAuthenticationProvider;
import com.example.registrationProject.repository.UserRepository;
import com.example.registrationProject.service.imp.UserServiceImpl;
import com.example.registrationProject.utility.JWTUtil;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

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
            "/otp/resend",
            "/swagger-ui/index.html",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
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
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST","PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customTomcatConfig() {
        return factory -> factory.addConnectorCustomizers(connector -> {
            connector.setMaxPartCount( 100); // Allow up to 100 parts in a multipart request
        });
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager,
                                                   JWTUtil jwtUtil) throws Exception {

        JWTAuthenticationFilter jwtAuthFilter= new JWTAuthenticationFilter(authenticationManager,jwtUtil);
        JwtValidationFilter jwtValidationFilter= new JwtValidationFilter(authenticationManager);
        JwtRefreshFilter jwtRefreshFilter= new JwtRefreshFilter(jwtUtil,authenticationManager);
        LogRequestFilter logRequestFilter= new LogRequestFilter();
        http
                .cors(withDefaults() )
                .authorizeHttpRequests(
                auth-> auth
                        .requestMatchers("/user/deleteAccount").hasAnyAuthority("DELETE_CONTENT")
                        .requestMatchers("/user/update/permission").hasAnyAuthority("ASSIGN_ROLE")
                        .requestMatchers("/user/update/role").hasAuthority("ASSIGN_ROLE")
                        .requestMatchers("/user/*").hasAnyAuthority("BROWSE_MUSIC")
                        .requestMatchers("/artist/update").hasAnyAuthority("ROLE_ARTIST","MANAGE_USER")
                        .requestMatchers("/playlist/*").hasAnyAuthority("BROWSE_MUSIC","ROLE_USER")
                        .requestMatchers("/track/add").hasAuthority("MANAGE_CONTENT")
                        .requestMatchers("/track/update").hasAnyAuthority("MANAGE_CONTENT","ROLE_SUPER_ADMIN")
                        .requestMatchers("/delete/*").hasAuthority("ROLE_SUPER_ADMIN")
                        .requestMatchers("/track/browsing").hasAnyAuthority("BROWSE_MUSIC")
                        .requestMatchers("/playlist/getAllPlaylists").hasAnyAuthority("ASSIGN_ROLE","ROLE_SUPER_ADMIN","MANAGE_CONTENT")
                        .requestMatchers("/playlist/*").hasAnyAuthority("BROWSE_MUSIC")
                        .requestMatchers("/album/create").hasAnyAuthority("MANAGE_CONTENT")
                        .requestMatchers("/label/update").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_LABEL","MANAGE_CONTENT")
                        .requestMatchers("/track/*").hasAnyAuthority("BROWSE_MUSIC")
                        .requestMatchers("/album/*").hasAnyAuthority("BROWSE_MUSIC")
                        .requestMatchers("/admin/*").hasAnyAuthority("ROLE_SUPER_ADMIN","ROLE_ADMIN")
                        .requestMatchers("/artist/*").hasAnyAuthority("ROLE_ARTIST","MANAGE_CONTENT")
                        .requestMatchers("/label/*").hasAnyAuthority("MANAGE_CONTENT")
                        .requestMatchers(whiteList).permitAll()
                        .anyRequest().authenticated()
        )
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtValidationFilter,JWTAuthenticationFilter.class)
                .addFilterAfter(jwtRefreshFilter, JwtValidationFilter.class)
                .addFilterAfter(logRequestFilter, JwtRefreshFilter.class)

                ;

                return http.build();

    }






    @Bean
    public AuthenticationManager authenticationManager(
            DaoAuthenticationProvider daoProvider,
            JWTAuthenticationProvider jwtProvider) {

        return new ProviderManager(Arrays.asList(daoProvider, jwtProvider));
    }







}

