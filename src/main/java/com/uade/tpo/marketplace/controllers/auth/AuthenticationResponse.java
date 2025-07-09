package com.uade.tpo.marketplace.controllers.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    @JsonProperty("access_token")
    private String accessToken;
    private String username;
    private String nombre;
    private String apellido;
    private String tipoUsuario; // Puede ser "gestor" o "usuario"
    private String email;
    private String telefono;
}
