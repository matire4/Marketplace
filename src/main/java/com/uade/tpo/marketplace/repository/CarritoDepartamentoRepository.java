package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.entities.CarritoDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarritoDepartamentoRepository extends JpaRepository<CarritoDepartamento, Long> {
    List<CarritoDepartamento> findByCarritoId(Long carritoId);
    void deleteByCarritoIdAndDepartamentoId(Long carritoId, Long departamentoId);
    boolean existsByCarritoIdAndDepartamentoId(Long carritoId, Long departamentoId);
}
