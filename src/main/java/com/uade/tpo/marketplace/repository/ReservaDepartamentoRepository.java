package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.ReservaDepartamento;

@Repository
public interface ReservaDepartamentoRepository extends JpaRepository<ReservaDepartamento, Long> {
    List<ReservaDepartamento> findByReservaId(Long reservaId);
    List<ReservaDepartamento> findByDepartamentoId(Long departamentoId);
}