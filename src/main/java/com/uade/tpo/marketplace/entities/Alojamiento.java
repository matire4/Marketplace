package com.uade.tpo.marketplace.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED) // esto cambia segun la forma de login q terminemos usando
@DiscriminatorColumn(name = "tipo_alojamiento", discriminatorType = DiscriminatorType.STRING)
public abstract class Alojamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private String direccion;
    @Column(nullable = false)
    private String ciudad;
    @Column(nullable = false)
    private String pais;

    @OneToMany(mappedBy = "alojamiento")
    private List<Review> reviews = new ArrayList<>();
    @OneToMany(mappedBy = "alojamiento")
    private List<Pregunta> preguntas = new ArrayList<>();
    @OneToMany(mappedBy = "alojamiento")
    private List<Imagen> imagenes = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "gestor_id", nullable = false)
    private Gestor gestor;
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Alojamiento(String descripcion, String direccion, String ciudad, String pais, Gestor gestor,
            Categoria categoria) {
        this.gestor = gestor;
        this.descripcion = descripcion;
        this.direccion = direccion;
        this.ciudad = ciudad;
        this.pais = pais;
        this.categoria = categoria;
        this.imagenes = new java.util.ArrayList<>();
    }
}
