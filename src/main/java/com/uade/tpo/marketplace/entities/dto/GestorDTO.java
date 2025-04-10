package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import lombok.Data;

@Data
public class GestorDTO {
    private Long id;
    private String nombre;
    private String cuil;
    private String username;
    private String password;
    private String email;
    private String telefono;
    private List<Long> hotelesIds;
}