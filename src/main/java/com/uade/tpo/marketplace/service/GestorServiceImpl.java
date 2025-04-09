package com.uade.tpo.marketplace.service;

import java.util.Optional;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.exceptions.GestorDuplicateException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;
import com.uade.tpo.marketplace.repository.GestorRepository;

@Service
public class GestorServiceImpl implements GestorService {
    @Autowired
    private GestorRepository gestorRepository;

    public Optional<Gestor> getGestorById(Long gestorId)
            throws GestorNotFoundException {
        return gestorRepository.findById(gestorId);
    }

    public Gestor createGestor(String username,
            String password,
            String email,
            String telefono,
            String nombre,
            String cuil)
            throws GestorDuplicateException {
        List<Gestor> gestores = gestorRepository.findByCuil(cuil);
        if (gestores.isEmpty())
            return gestorRepository.save(new Gestor(
                    username,
                    password,
                    email,
                    telefono,
                    nombre,
                    cuil));
        throw new GestorDuplicateException();
    }
}
