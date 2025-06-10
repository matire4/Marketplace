package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Departamento;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.dto.DepartamentoDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.repository.CategoriaRepository;
import com.uade.tpo.marketplace.repository.DepartamentoRepository;
import com.uade.tpo.marketplace.repository.GestorRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {
    
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private GestorRepository gestorRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public List<DepartamentoDTO> getDepartamentos() {
        List<Departamento> departamentos = departamentoRepository.findAll();
        return departamentos.stream().map(this::departamentoToDepartamentoDTO).toList();
    }

    @Override
    public Optional<Departamento> getDepartamentoById(Long departamentoId) throws DepartamentoNotFoundException {
        return departamentoRepository.findById(departamentoId);
    }

    @Override
    @Transactional
    public Departamento createDepartamento(
            int capacidad,
            double precioPorNoche,
            String numeroDepartamento,
            String descripcion,
            String direccion,
            String imagen,
            Long gestorId,
            Long categoriaId) throws GestorNotFoundException, CategoriaNotFoundException {

        Gestor gestor = gestorRepository.findById(gestorId)
                .orElseThrow(() -> new GestorNotFoundException());
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new CategoriaNotFoundException());

        Departamento departamento = Departamento.builder()
                .capacidad(capacidad)
                .precioPorNoche(precioPorNoche)
                .numeroDepartamento(numeroDepartamento)
                .descripcion(descripcion)
                .direccion(direccion)
                .imagen(imagen)
                .gestor(gestor)
                .categoria(categoria)
                .build();

        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento updateDepartamento(Long departamentoId, DepartamentoDTO departamentoDTO)
            throws DepartamentoNotFoundException, GestorNotFoundException, CategoriaNotFoundException {
        
        Departamento departamento = departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new DepartamentoNotFoundException());

        Gestor gestor = gestorRepository.findById(departamentoDTO.getGestorId())
                .orElseThrow(() -> new GestorNotFoundException());
        Categoria categoria = categoriaRepository.findById(departamentoDTO.getCategoriaId())
                .orElseThrow(() -> new CategoriaNotFoundException());

        departamento.setCapacidad(departamentoDTO.getCapacidad());
        departamento.setPrecioPorNoche(departamentoDTO.getPrecioPorNoche());
        departamento.setNumeroDepartamento(departamentoDTO.getNumeroDepartamento());
        departamento.setDescripcion(departamentoDTO.getDescripcion());
        departamento.setDireccion(departamentoDTO.getDireccion());
        departamento.setImagen(departamentoDTO.getImagen());
        departamento.setGestor(gestor);
        departamento.setCategoria(categoria);

        return departamentoRepository.save(departamento);
    }

    @Override
    public void deleteDepartamento(Long departamentoId) throws DepartamentoNotFoundException {
        if (!departamentoRepository.existsById(departamentoId)) {
            throw new DepartamentoNotFoundException();
        }
        departamentoRepository.deleteById(departamentoId);
    }

    @Override
    public DepartamentoDTO departamentoToDepartamentoDTO(Departamento departamento) {
        return DepartamentoDTO.builder()
                .id(departamento.getId())
                .capacidad(departamento.getCapacidad())
                .precioPorNoche(departamento.getPrecioPorNoche())
                .numeroDepartamento(departamento.getNumeroDepartamento())
                .descripcion(departamento.getDescripcion())
                .direccion(departamento.getDireccion())
                .imagen(departamento.getImagen())
                .gestorId(departamento.getGestor().getId())
                .categoriaId(departamento.getCategoria().getId())
                .build();
    }

    @Override
    public List<DepartamentoDTO> getDepartamentosDisponibles() {
        return departamentoRepository.findByDisponibleTrue();
    }
}