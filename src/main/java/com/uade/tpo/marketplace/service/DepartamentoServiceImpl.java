package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Departamento;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.Imagen;
import com.uade.tpo.marketplace.entities.dto.DepartamentoDTO;
import com.uade.tpo.marketplace.entities.dto.ImagenDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.repository.CategoriaRepository;
import com.uade.tpo.marketplace.repository.DepartamentoRepository;
import com.uade.tpo.marketplace.repository.GestorRepository;
import com.uade.tpo.marketplace.repository.ImagenRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {
    
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private GestorRepository gestorRepository;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ImagenRepository imagenRepository;

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
            String ciudad,
            String pais,
            String descripcion,
            String direccion,
            List<Long> imagenes,
            List<ImagenDTO> imagenCrear,
            String username,
            String categoria) throws GestorNotFoundException, CategoriaNotFoundException {

        Gestor gestor = gestorRepository.findByUsername(username)
                .orElseThrow(() -> new GestorNotFoundException());
        Categoria c = categoriaRepository.findByNombre(categoria)
                .orElseThrow(() -> new CategoriaNotFoundException());

        Departamento departamento = new Departamento( descripcion, direccion, ciudad, pais, gestor, c, capacidad, precioPorNoche, numeroDepartamento);

        return departamentoRepository.save(departamento);
    }

    @Override
    public Departamento updateDepartamento(Long departamentoId, DepartamentoDTO departamentoDTO)
            throws DepartamentoNotFoundException, GestorNotFoundException, CategoriaNotFoundException {
        
        Departamento departamento = departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new DepartamentoNotFoundException());

        Gestor gestor = gestorRepository.findByUsername(departamentoDTO.getUsername())
                .orElseThrow(() -> new GestorNotFoundException());
        Categoria categoria = categoriaRepository.findByNombre(departamentoDTO.getCategoria())
                .orElseThrow(() -> new CategoriaNotFoundException());
        List<Imagen> imagenes = departamentoDTO.getImagenesNuevas().stream()
                .map(imagenDTO -> imagenRepository.findById(imagenDTO.getId())
                        .orElseThrow(() -> new RuntimeException()))
                .toList();

        departamento.setCapacidad(departamentoDTO.getCapacidad());
        departamento.setPrecioPorNoche(departamentoDTO.getPrecioPorNoche());
        departamento.setNumeroDepartamento(departamentoDTO.getNumeroDepartamento());
        departamento.setDescripcion(departamentoDTO.getDescripcion());
        departamento.setDireccion(departamentoDTO.getDireccion());
        departamento.setImagenes(imagenes);
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
                .imagenes(departamento.getImagenes().stream().map(i -> i.getId()).toList())
                .username(departamento.getGestor().getUsername())
                .categoria(departamento.getCategoria().getNombre())
                .build();
    }
}