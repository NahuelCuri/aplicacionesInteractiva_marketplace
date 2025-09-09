package com.uade.tpo.Marketplace.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll()
                        
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/products").hasAuthority("SELLER")
                        
                        .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                        
                        .requestMatchers(HttpMethod.GET, "/product-images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/product-images/**").hasAuthority("SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/product-images/**").hasAuthority("SELLER")
                        .requestMatchers("/order/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").authenticated()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
