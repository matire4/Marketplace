package com.uade.tpo.marketplace.entities.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlojamientoDTO {
    private Long id;
    private String descripcion;
    private String direccion;
    private String ciudad;
    private String pais;
    private Long gestorId;
    private Long categoriaId;
    private List<Long> reviews;
    private List<Long> preguntas;
    private List<Long> imagenes;
}