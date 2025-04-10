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

            // Solo admin puede crear/editar/borrar categorías
            .requestMatchers(HttpMethod.POST, "/api/v1/categorias/**").hasRole("ADMINISTRADOR")
            .requestMatchers(HttpMethod.PUT, "/api/v1/categorias/**").hasRole("ADMINISTRADOR")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/categorias/**").hasRole("ADMINISTRADOR")

            // Solo gestor puede crear hoteles y habitaciones
            .requestMatchers(HttpMethod.POST, "/api/v1/hoteles/**").hasRole("GESTOR")
            .requestMatchers(HttpMethod.POST, "/api/v1/habitaciones/**").hasRole("GESTOR")

            // Ver carrito requiere estar logueado
            .requestMatchers("/api/v1/carrito/**").authenticated()

            // Cualquier otra cosa requiere autenticación
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}
        
}
