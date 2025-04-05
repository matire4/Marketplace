package com.uade.tpo.marketplace.entities.dto;

import lombok.Data;

@Data
public class PreguntaDTO {
    private Long id;
    private String pregunta;
    private String respuesta;
    private Long hotelId;
    private Long usuarioId;
    private Long habitacionId;
}