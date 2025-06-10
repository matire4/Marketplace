package com.uade.tpo.marketplace.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("departamento")
@EqualsAndHashCode(callSuper = true)
@Entity
public class Departamento extends Alojamiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    int capacidad;
    @Column(nullable = false)
    double precioPorNoche;
    @Column(nullable = false)
    String numeroDepartamento;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CarritoDepartamento> carritoDepartamento;
    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReservaDepartamento> reservasDepartamento;

    @ManyToOne
    @JoinColumn(name = "gestor_id", nullable = false)
    private Gestor gestor;
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Departamento(String descripcion, String direccion, String ciudad, String pais, Gestor gestor, Categoria categoria, int capacidad, double precioPorNoche, String numeroDepartamento) {
        super(descripcion, direccion, ciudad, pais, gestor, categoria);
        this.capacidad = capacidad;
        this.precioPorNoche = precioPorNoche;
        this.numeroDepartamento = numeroDepartamento;
    }
}