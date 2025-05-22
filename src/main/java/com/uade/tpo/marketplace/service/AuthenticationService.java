package com.uade.tpo.marketplace.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.controllers.auth.AuthenticationRequest;
import com.uade.tpo.marketplace.controllers.auth.AuthenticationResponse;
import com.uade.tpo.marketplace.controllers.auth.RegisterRequest;
import com.uade.tpo.marketplace.controllers.config.JwtService;
import com.uade.tpo.marketplace.entities.Cuenta;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.enums.Role;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.repository.CuentaRepository;
import com.uade.tpo.marketplace.repository.GestorRepository;
import com.uade.tpo.marketplace.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UsuarioRepository usuarioRepository; // aca iria un cuenta repository
        private final CuentaRepository cuentaRepository;
        private final GestorRepository gestorRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse  register(RegisterRequest request) {
                if (request.getRole() == Role.ADMINISTRADOR || request.getRole() == Role.CLIENTE) {
                       Usuario user = new Usuario(
                                request.getName(),
                                request.getLastname(),
                                request.getRole(),
                                request.getEmail().split("@")[0],
                                passwordEncoder.encode(request.getPassword()),
                                request.getEmail(),
                                request.getPhone());
                        usuarioRepository.save(user);
                        var jwtToken = jwtService.generateToken(user);
                        return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build(); 
                }
                else {
                        Gestor gestor = new Gestor(
                                request.getEmail().split("@")[0],
                                passwordEncoder.encode(request.getPassword()),
                                request.getEmail(),
                                request.getPhone(),
                                request.getName(),
                                request.getCuil());
                        gestorRepository.save(gestor);
                        var jwtToken = jwtService.generateToken(gestor);
                        return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build(); 
                }
                
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) throws UsuarioNotFoundException,GestorNotFoundException{
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getUsername(),
                                                request.getPassword()));
                System.out.println("Username: " + request.getUsername());
                Cuenta user = cuentaRepository.findByUsername(request.getUsername());
                if (user == null) {
                        throw new UsuarioNotFoundException();
                }
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }
}
