package com.example.registrationProject.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

public class LogRequestFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain)
            throws IOException, ServletException

    {
        String user = request.getRemoteUser();

        logger.info("Request hit " +request.getMethod() + " " + request.getRequestURI());

        filterChain.doFilter(request, response);
    }
}
