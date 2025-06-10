package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Departamento;
import com.uade.tpo.marketplace.entities.dto.DepartamentoDTO;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Long> {
    List<DepartamentoDTO> findByDisponibleTrue();
}