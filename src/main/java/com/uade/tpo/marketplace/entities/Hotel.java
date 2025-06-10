package com.uade.tpo.marketplace.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@DiscriminatorValue("hotel")
@EqualsAndHashCode(callSuper = true)
@Table(name = "Hotel")
public class Hotel extends Alojamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false, unique = true)
    private String telefono;
    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Habitacion> habitaciones = new ArrayList<>();

    public Hotel(String descripcion, String direccion, String ciudad, String pais, Gestor gestor, Categoria categoria, String nombre, String telefono, String email) {
        super(descripcion, direccion, ciudad, pais, gestor, categoria);
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
    }
}
