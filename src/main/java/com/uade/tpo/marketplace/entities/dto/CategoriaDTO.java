package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import lombok.Data;

@Data
public class CategoriaDTO {
    private Long id;
    private String nombre;
    private List<Long> hotelesIds;
}