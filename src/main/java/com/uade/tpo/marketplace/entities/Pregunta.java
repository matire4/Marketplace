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
public class Pregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String pregunta;
    @Column
    private String respuesta;

    @ManyToOne
    @Column(name = "hotel_id", nullable = false)
    private Hotel hotel;
    @ManyToOne
    @Column(name = "usuario_id", nullable = false)
    private Usuario usuario;
    @ManyToOne
    @Column(name = "habitacion_id")
    private Habitacion habitacion;

    public Pregunta() {
    }

    public Pregunta(String pregunta, String respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }
}
