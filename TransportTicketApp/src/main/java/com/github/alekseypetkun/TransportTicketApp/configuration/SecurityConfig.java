package com.github.alekseypetkun.TransportTicketApp.configuration;


import com.github.alekseypetkun.TransportTicketApp.security.filter.JwtFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-doc/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/api/auth/login",
            "/api/auth/token",
            "/api/auth/refresh",
            "/api/auth/register",
            "/api/tickets"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf()
                .disable()
                .authorizeHttpRequests(
                        (authorization) ->
                        {
                            try {
                                authorization
                                        .requestMatchers(AUTH_WHITELIST).permitAll()
                                        .requestMatchers(GET, "/tickets").permitAll()
                                        .anyRequest().authenticated()
                                        .and()
                                        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).build();
    }
}
