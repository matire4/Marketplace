package com.uade.tpo.marketplace.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double rating;
    @Column(nullable = false)
    private String comment;

    @ManyToOne
    @Column(name = "user_id", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @Column(name = "hotel_id", nullable = false)
    private Hotel hotel;
}
