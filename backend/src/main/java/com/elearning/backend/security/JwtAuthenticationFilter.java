package com.elearning.backend.security;

import io.jsonwebtoken.Claims;
import jakarta.persistence.Column;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    public JwtAuthenticationFilter(JwtService jwtService){
        this.jwtService = jwtService;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        if(uri.contains("/uploads")){
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        try{
            Claims claims = jwtService.parseToken(token);
            String email = claims.getSubject();
            String role = ((String) claims.get("role")).replace("ROLE_", "");

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                var authority = new SimpleGrantedAuthority("ROLE_"+role);
                var authToken =  new UsernamePasswordAuthenticationToken(email,null, List.of(authority));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                log.info("Parsed JWT Email :"+ email + " | Role:"+authority);
                log.info("Authentication set in context with authority:"+authority.getAuthority());
            }
        } catch (Exception e){
            System.out.println("Invalid JWT:" + e.getMessage());
        }
        filterChain.doFilter(request,response);

        log.info("JwtAuthenticationFilter Triggered for URL: {} ",request.getRequestURI());
        log.info("Authentication Header:{}", authHeader);

    }
}


