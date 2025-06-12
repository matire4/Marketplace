package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Gestor;
import com.uade.tpo.marketplace.entities.dto.GestorDTO;
import com.uade.tpo.marketplace.exceptions.GestorDuplicateException;
import com.uade.tpo.marketplace.exceptions.GestorNotFoundException;

public interface GestorService {

        public List<GestorDTO> getGestores();

        public Optional<Gestor> getGestorById(Long gestorId) throws GestorNotFoundException;

        public Optional<Gestor> getGestorByUsername(String username) throws GestorNotFoundException;

        public Gestor createGestor(String username, String password, String email, String telefono, String nombre,
                        String cuil)
                        throws GestorDuplicateException;

        public Gestor updateGestor(Long gestorId, String username, String password, String email, String telefono,
                        String nombre, String cuil)
                        throws GestorNotFoundException, GestorDuplicateException;

        public void deleteGestor(Long gestorId) throws GestorNotFoundException;

        public GestorDTO gestorToGestorDTO(Gestor gestor);
}
