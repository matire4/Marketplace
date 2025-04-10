package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.dto.GestorDTO;
import com.uade.tpo.marketplace.exceptions.GestorDuplicateException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.repository.GestorRepository;

@Service
public class GestorServiceImpl implements GestorService {
    @Autowired
    private GestorRepository gestorRepository;

    @Override
    public List<GestorDTO> getGestores() {
        List<Gestor> gestores = gestorRepository.findAll();
        return gestores.stream().map(gestor -> this.gestorToGestorDTO(gestor)).toList();
    }

    @Override
    public Optional<Gestor> getGestorById(Long gestorId) throws GestorNotFoundException {
        return Optional.ofNullable(
                gestorRepository.findById(gestorId)
                        .orElseThrow(() -> new GestorNotFoundException()));
    }

    @Override
    public Gestor createGestor(String username, String password, String email, String telefono, String nombre,
            String cuil)
            throws GestorDuplicateException {
        if (gestorRepository.existsByUsername(username))
            throw new GestorDuplicateException();

        if (gestorRepository.existsByEmail(email))
            throw new GestorDuplicateException();

        Gestor gestor = new Gestor(username, password, email, telefono, nombre, cuil);
        return gestorRepository.save(gestor);
    }

    @Override
    public Gestor updateGestor(Long gestorId, String username, String password, String email, String telefono,
            String nombre, String cuil)
            throws GestorNotFoundException, GestorDuplicateException {
        Gestor gestor = gestorRepository.findById(gestorId)
                .orElseThrow(() -> new GestorNotFoundException());

        if (gestorRepository.existsByUsernameAndIdNot(username, gestorId)) {
            throw new GestorDuplicateException();
        }
        if (gestorRepository.existsByEmailAndIdNot(email, gestorId)) {
            throw new GestorDuplicateException();
        }

        gestor.setUsername(username);
        gestor.setPassword(password);
        gestor.setEmail(email);
        gestor.setTelefono(telefono);
        gestor.setNombre(nombre);
        gestor.setCuil(cuil);

        return gestorRepository.save(gestor);
    }

    @Override
    public void deleteGestor(Long gestorId) throws GestorNotFoundException {
        Gestor gestor = gestorRepository.findById(gestorId)
                .orElseThrow(() -> new GestorNotFoundException());

        gestorRepository.delete(gestor);
    }

    @Override
    public GestorDTO gestorToGestorDTO(Gestor gestor) {
        GestorDTO gestorDTO = new GestorDTO();
        gestorDTO.setId(gestor.getId());
        gestorDTO.setUsername(gestor.getUsername());
        gestorDTO.setEmail(gestor.getEmail());
        gestorDTO.setTelefono(gestor.getTelefono());
        gestorDTO.setNombre(gestor.getNombre());
        gestorDTO.setCuil(gestor.getCuil());
        return gestorDTO;
    }
}
