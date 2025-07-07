package com.uade.tpo.marketplace.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Imagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "LONGTEXT")
    String imagen;

    @ManyToOne
    @JoinColumn(name = "alojamiento_id", nullable = true)
    private Alojamiento alojamiento;

    @ManyToOne
    @JoinColumn(name = "habitacion_id", nullable = true)
    private Habitacion habitacion;
}
