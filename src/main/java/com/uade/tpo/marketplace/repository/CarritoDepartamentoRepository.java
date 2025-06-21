package com.uade.tpo.marketplace.repository;

import com.uade.tpo.marketplace.entities.CarritoDepartamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CarritoDepartamentoRepository extends JpaRepository<CarritoDepartamento, Long> {
    List<CarritoDepartamento> findByCarritoId(Long carritoId);
    
    boolean existsByCarritoIdAndDepartamentoId(Long carritoId, Long departamentoId);

    @Query("SELECT cd FROM CarritoDepartamento cd WHERE cd.carrito_id = :carritoId AND cd.departamento_id = :departamentoId")
    void deleteByCarritoIdAndDepartamentoId(@Param("carritoId") Long carritoId, @Param("departamentoId") Long departamentoId);
}
