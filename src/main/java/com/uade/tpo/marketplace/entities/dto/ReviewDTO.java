package com.uade.tpo.marketplace.entities.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private double rating;
    private String comment;
    private String username;
    private Long alojamientoId;
    private String alojamientoDescripcion;

    public ReviewDTO() {
    }

    public ReviewDTO(Long id, double rating, String comment, String username, Long alojamientoId, String alojamientoDescripcion) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.username = username;
        this.alojamientoId = alojamientoId;
        this.alojamientoDescripcion = alojamientoDescripcion;
    }
}