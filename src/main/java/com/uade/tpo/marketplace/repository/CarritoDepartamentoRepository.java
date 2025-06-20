package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.entities.CarritoDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarritoDepartamentoRepository extends JpaRepository<CarritoDepartamento, Long> {
    List<CarritoDepartamento> findByCarritoId(Long carritoId);
    
    boolean existsByCarritoIdAndDepartamentoId(Long carritoId, Long departamentoId);

    @Query("SELECT cd FROM CarritoDepartamento cd WHERE cd.carrito.id = ?1 AND cd.departamento.id = ?2")
    void deleteByCarritoIdAndDepartamentoId(Long carritoId, Long departamentoId);
}
