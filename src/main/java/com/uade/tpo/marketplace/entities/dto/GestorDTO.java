package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import lombok.Data;

@Data
public class GestorDTO {
    private Long id;
    private String nombre;
    private String cuil;
    private List<Long> hotelesIds;
}