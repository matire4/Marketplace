package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import lombok.Data;

import com.uade.tpo.marketplace.enums.Role;

@Data
public class GestorDTO {
    private Long id;
    private String nombre;
    private String cuil;
    private String username;
    private String password;
    private String email;
    private String telefono;
    private Role role;
    private List<Long> hotelesIds;
}