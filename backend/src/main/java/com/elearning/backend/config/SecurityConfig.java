package com.elearning.backend.config;

import com.elearning.backend.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/docs").permitAll()

                        .requestMatchers("/api/auth/**").permitAll()

                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/images/**","/css/**","/js/**","/uploads/**").permitAll()

                        .requestMatchers("/api/courses/instructor/**").hasRole("INSTRUCTOR")
                        .requestMatchers("/api/test/instructor").hasRole("INSTRUCTOR")
                        .requestMatchers("/api/instructors/**").hasRole("INSTRUCTOR")

                        .requestMatchers(HttpMethod.OPTIONS,"/**").permitAll()

                        .requestMatchers("/api/notifications/**").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.POST, "/api/announcements/**").hasRole("INSTRUCTOR")
                        .requestMatchers(HttpMethod.GET,  "/api/announcements/**").hasAnyRole("INSTRUCTOR","STUDENT")
                        .requestMatchers("/api/notifications/**").authenticated()
                        .requestMatchers("/api/announcements/**").authenticated()

                        .requestMatchers("/React/**").hasRole("STUDENT")
                        .requestMatchers("/api/courses/students/**", "/api/enrollments/**").hasRole("STUDENT")
                        .requestMatchers("/api/payments/**").hasAnyRole("STUDENT", "INSTRUCTOR")
                        .requestMatchers("/api/test/student").hasRole("STUDENT")

                       .requestMatchers("/api/courses/**").hasAnyRole("INSTRUCTOR", "STUDENT")

                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/assessment-attempts").hasRole("STUDENT")
                        .requestMatchers(org.springframework.http.HttpMethod.GET,  "/api/assessment-attempts").hasRole("STUDENT")
                        .requestMatchers(org.springframework.http.HttpMethod.GET,  "/api/assessment-attempts/by-assessment/**").hasRole("INSTRUCTOR")


                        //    .requestMatchers("/api/courses/**").permitAll()
                    //    .requestMatchers("/api/courses/**").hasAnyRole("INSTRUCTOR", "STUDENT")
                        .anyRequest().authenticated()

                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
