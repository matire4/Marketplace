package com.uade.tpo.marketplace.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String telefono;
    @Column(nullable = false, unique = true)
    private String email;
    @Column
    private String descripcion;
    @Column(nullable = false)
    private String direccion;
    @Column(nullable = false)
    private String ciudad;
    @Column(nullable = false)
    private String pais;

    @OneToMany(mappedBy = "hotel")
    private List<Habitacion> habitaciones;
    @OneToMany(mappedBy = "hotel")
    private List<Review> reviews;
    @OneToMany(mappedBy = "hotel")
    private List<Pregunta> preguntas;

    @ManyToOne
    @Column(name = "gestor_id", nullable = false)
    private Gestor gestor;
    @ManyToOne
    @Column(name = "categoria_id", nullable = false)
    private Categoria categoria;
}
