package com.uade.tpo.marketplace.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.uade.tpo.marketplace.entities.Categoria;
import com.uade.tpo.marketplace.entities.Departamento;
import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.Imagen;
import com.uade.tpo.marketplace.entities.dto.DepartamentoDTO;
import com.uade.tpo.marketplace.exceptions.CategoriaNotFoundException;
import com.uade.tpo.marketplace.exceptions.DepartamentoNotFoundException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.repository.DepartamentoRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {
    
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private GestorService gestorService;
    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private ImagenService imagenService;

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
            String breveDescripcion,
            String descripcion,
            String direccion,
            int ambientes,
            int banos,
            int dormitorios,
            int camas,
            List<MultipartFile> imagenesNuevas,
            String username,
            String categoria) throws GestorNotFoundException, CategoriaNotFoundException {

        Gestor gestor = gestorService.getGestorByUsername(username)
                .orElseThrow(() -> new GestorNotFoundException());
        Categoria c = categoriaService.getCategoriaByNombre(categoria)
                .orElseThrow(() -> new CategoriaNotFoundException());
        List<Imagen> imagenes = new ArrayList<>();
        if (imagenesNuevas != null) {
            for (MultipartFile imagen : imagenesNuevas) {
                Imagen newImagen = new Imagen();
                try {
                    newImagen.setImagen(Base64.getEncoder().encodeToString(imagen.getBytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imagenes.add(imagenService.save(newImagen));
            }
        }

        Departamento departamento = new Departamento(breveDescripcion, descripcion, direccion, ciudad, pais, gestor, c, imagenes, capacidad, precioPorNoche, numeroDepartamento, ambientes, banos, dormitorios, camas);
        Departamento savedDepartamento = departamentoRepository.save(departamento);
        for (Imagen imagen : imagenes) {
            imagen.setAlojamiento(savedDepartamento);
            imagenService.save(imagen);
        }

        return savedDepartamento;
    }

    @Override
    @Transactional
    public Departamento updateDepartamento(Long departamentoId, DepartamentoDTO departamentoDTO)
            throws DepartamentoNotFoundException, GestorNotFoundException, CategoriaNotFoundException, IOException{
        
        Departamento departamento = departamentoRepository.findById(departamentoId)
                .orElseThrow(() -> new DepartamentoNotFoundException());

        Gestor gestor = gestorService.getGestorByUsername(departamentoDTO.getUsername())
                .orElseThrow(() -> new GestorNotFoundException());
        Categoria categoria = categoriaService.getCategoriaByNombre(departamentoDTO.getCategoria())
                .orElseThrow(() -> new CategoriaNotFoundException());
        if (departamentoDTO.getImagenesNuevas() != null) {
            List<Imagen> imagenes = departamentoDTO.getImagenesNuevas().stream()
                    .map(imagen -> {
                        Imagen newImagen = new Imagen();
                        try {
                            newImagen.setImagen(imagen.getBytes().toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return imagenService.save(newImagen);
                    })
                    .toList();
                departamento.setImagenes(new ArrayList<>(imagenes));
        }

        departamento.setCapacidad(departamentoDTO.getCapacidad());
        departamento.setPrecioPorNoche(departamentoDTO.getPrecioPorNoche());
        departamento.setNumeroDepartamento(departamentoDTO.getNumeroDepartamento());
        departamento.setDescripcion(departamentoDTO.getDescripcion());
        departamento.setDireccion(departamentoDTO.getDireccion());
        departamento.setGestor(gestor);
        departamento.setCategoria(categoria);
        List<Imagen> currentImages = new ArrayList<>();
        if (departamentoDTO.getImagenesIds() != null) {
                currentImages = departamentoDTO.getImagenesIds().stream()
                        .map(imagenId -> {
                                Imagen newImagen = new Imagen();
                                newImagen.setId(imagenId);
                                return imagenService.save(newImagen);
                        }).toList();
        }
        currentImages.addAll(departamentoDTO.getImagenesNuevas().stream()
                        .map(imagen -> {
                                Imagen newImagen = new Imagen();
                                try {
                                        newImagen.setImagen(Base64.getEncoder().encodeToString(imagen.getBytes()));
                                } catch (IOException e) {
                                        e.printStackTrace();
                                }
                                return imagenService.save(newImagen);
                        }).toList());
        departamento.setImagenes(currentImages);

        return departamentoRepository.save(departamento);
    }

    @Override
    @Transactional
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
                .ambientes(departamento.getAmbientes())
                .banos(departamento.getBanos())
                .dormitorios(departamento.getDormitorios())
                .camas(departamento.getCamas())
                .breveDescripcion(departamento.getBreveDescripcion())
                .ciudad(departamento.getCiudad())
                .pais(departamento.getPais())
                // Solo enviar IDs de imágenes, no las imágenes completas
                .imagenesIds(departamento.getImagenes().stream().map(i -> i.getId()).toList())
                .username(departamento.getGestor().getUsername())
                .categoria(departamento.getCategoria().getNombre())
                .build();
    }
}