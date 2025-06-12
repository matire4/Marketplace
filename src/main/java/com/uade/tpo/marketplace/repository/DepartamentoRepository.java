package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Departamento;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    List<Departamento> findByCapacidad(int capacidad);
    List<Departamento> findByPrecioPorNoche(double precioPorNoche);
    List<Departamento> findByCiudad(String ciudad);
    List<Departamento> findByPais(String pais);
}