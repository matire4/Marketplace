package com.uade.tpo.marketplace.repository;

import java.util.Optional;
import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplace.entities.Habitacion;
import com.uade.tpo.marketplace.entities.Reserva;
import com.uade.tpo.marketplace.entities.ReservaHabitacion;
import com.uade.tpo.marketplace.enums.Estado;

@Repository
public interface ReservaHabitacionRepository extends JpaRepository<ReservaHabitacion, Long> {
    Optional<ReservaHabitacion> findById(Long id);
    List<ReservaHabitacion> findByReservaId(Long reservaId);
    List<ReservaHabitacion> findByHabitacionId(Long habitacionId);
    List<ReservaHabitacion> findByReservaAndHabitacion(Reserva reserva, Habitacion habitacion);
    List<ReservaHabitacion> findByEstado(Estado estado);
    List<ReservaHabitacion> findByFechaDesde(Date fechaDesde);
    List<ReservaHabitacion> findByFechaHasta(Date fechaHasta);
    List<ReservaHabitacion> findByPrecio(double precio);
}
