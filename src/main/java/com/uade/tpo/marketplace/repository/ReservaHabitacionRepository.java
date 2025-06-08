package com.uade.tpo.marketplace.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.ReservaHabitacion;

@Repository
public interface ReservaHabitacionRepository extends JpaRepository<ReservaHabitacion, Long> {
    List<ReservaHabitacion> findByReservaId(Long reservaId);
    List<ReservaHabitacion> findByHabitacionId(Long habitacionId);
    List<ReservaHabitacion> findByReservaIdAndHabitacionId(Long reservaId, Long habitacionId);
    List<ReservaHabitacion> findByEstado(String estado);
}
