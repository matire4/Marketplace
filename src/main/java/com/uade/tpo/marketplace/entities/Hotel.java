package com.uade.tpo.marketplace.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Hotel")
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

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habitacion> habitaciones = new ArrayList<>();
    @OneToMany(mappedBy = "hotel")
    private List<Review> reviews;
    @OneToMany(mappedBy = "hotel")
    private List<Pregunta> preguntas;

    @ManyToOne
    @JoinColumn(name = "gestor_id", nullable = false)
    private Gestor gestor;
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Hotel() {
    }

    public Hotel(String nombre, String telefono, String email, String description, String direccion, String ciudad,
            String pais, Gestor gestor, Categoria categoria) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.descripcion = description;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.gestor = gestor;
        this.categoria = categoria;
        this.habitaciones = new ArrayList<Habitacion>();
    }
}
