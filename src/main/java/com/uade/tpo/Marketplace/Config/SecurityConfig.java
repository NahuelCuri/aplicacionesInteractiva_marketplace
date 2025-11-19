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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/categories/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/product-images/**").permitAll()

                        // Seller endpoints
                        .requestMatchers(HttpMethod.POST, "/products").hasAuthority("SELLER")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasAuthority("SELLER")
                        .requestMatchers(HttpMethod.POST, "/product-images/**").hasAuthority("SELLER")
                        .requestMatchers(HttpMethod.DELETE, "/product-images/**").hasAuthority("SELLER")

                        // Admin endpoints
                        .requestMatchers("/roles/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/categories/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/users").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/**").hasAuthority("ADMIN")

                        // Authenticated user endpoints
                        .requestMatchers(HttpMethod.POST, "/order/cart/items").hasAnyAuthority("BUYER", "SELLER", "ADMIN")
                        .requestMatchers("/order/**").authenticated()
                        .requestMatchers(HttpMethod.GET, "/users/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/users/**").authenticated()

                        // Deny all other requests by default
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5175", "http://localhost:5173"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
