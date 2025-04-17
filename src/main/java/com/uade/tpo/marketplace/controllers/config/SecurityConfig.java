package com.uade.tpo.marketplace.controllers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import com.uade.tpo.marketplace.enums.RolUsuario;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;

        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
        .requestMatchers("/api/v1/auth/**").permitAll()

        .requestMatchers(HttpMethod.POST, "/api/v1/categorias/**").permitAll()
        .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasRole(RolUsuario.Administrador.name().toUpperCase())
        .requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasRole(RolUsuario.Administrador.name().toUpperCase())
        
        .requestMatchers(HttpMethod.GET, "/api/v1/categorias/**").authenticated()
        .requestMatchers(HttpMethod.GET, "/api/v1/hoteles/**").authenticated()
        .requestMatchers(HttpMethod.POST, "/api/v1/hoteles/**").authenticated()
        .requestMatchers(HttpMethod.PUT, "/api/v1/hoteles/**").authenticated()
        .requestMatchers(HttpMethod.GET, "/api/v1/gestores/**").authenticated()
        .requestMatchers(HttpMethod.POST, "/api/v1/gestores/**").authenticated()
        .requestMatchers(HttpMethod.PUT, "/api/v1/gestores/**").authenticated()

        .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/**").authenticated()
        .requestMatchers(HttpMethod.POST, "/api/v1/usuarios/**").authenticated()
        .requestMatchers(HttpMethod.PUT, "/api/v1/usuarios/**").authenticated()
        
        .requestMatchers("/api/v1/carrito/**").authenticated()
        
        .anyRequest().authenticated()
        
        )
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
        
}
