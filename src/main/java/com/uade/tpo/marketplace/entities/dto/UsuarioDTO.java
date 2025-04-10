package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import com.uade.tpo.marketplace.enums.RolUsuario;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String username;
    private String password;
    private String email;
    private String telefono;
    private RolUsuario rolUsuario;
    private List<Long> reservasIds;
    private List<Long> reviewsIds;
    private Long carritoId;
}