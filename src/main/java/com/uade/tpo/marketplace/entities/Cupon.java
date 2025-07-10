package com.uade.tpo.marketplace.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Cupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String codigo;
    @Column(nullable = false)
    private double descuento;

    public Cupon() {
    }

    public Cupon(String codigo, double descuento) {
        this.codigo = codigo;
        this.descuento = descuento;
    }
}
