package com.uade.tpo.marketplace.service;

import java.util.Optional;

import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.dto.GestorDTO;
import com.uade.tpo.marketplace.exceptions.GestorDuplicateException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;

public interface GestorService {
    Optional<Gestor> getGestorById(Long gestorId) throws GestorNotFoundException;

    Gestor createGestor(String username, String password, String email, String telefono, String nombre, String cuil)
            throws GestorDuplicateException;

    Gestor updateGestor(Long gestorId, String username, String password, String email, String telefono, String nombre, String cuil)
            throws GestorNotFoundException, GestorDuplicateException;

    void deleteGestor(Long gestorId) throws GestorNotFoundException;

    GestorDTO gestorToGestorDTO(Gestor gestor);
}
