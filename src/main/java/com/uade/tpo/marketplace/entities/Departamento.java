package com.uade.tpo.marketplace.entities;

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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Departamento { // se peude hacer como clase hija, hermana de habitacion, revisar 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    int capacidad;
    @Column
    double precioPorNoche;
    @Column
    String numeroDepartamento;
    @Column
    String descripcion;
    @Column
    String direccion;
    @Column
    String imagen;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoDepartamento> carritoDepartamento;

    @ManyToOne
    @JoinColumn(name = "gestor_id", nullable = false)
    private Gestor gestor;
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}       