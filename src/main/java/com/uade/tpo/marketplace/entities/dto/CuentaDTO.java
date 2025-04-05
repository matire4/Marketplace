package com.uade.tpo.marketplace.entities.dto;

import lombok.Data;

@Data
public class CuentaDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String telefono;
}