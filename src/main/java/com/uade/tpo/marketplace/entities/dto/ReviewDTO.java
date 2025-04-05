package com.uade.tpo.marketplace.entities.dto;

import lombok.Data;

@Data
public class ReviewDTO {
    private Long id;
    private double rating;
    private String comment;
    private Long usuarioId;
    private Long hotelId;
}