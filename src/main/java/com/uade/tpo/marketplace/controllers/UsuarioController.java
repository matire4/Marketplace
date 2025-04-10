package com.uade.tpo.marketplace.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.entities.dto.UsuarioDTO;
import com.uade.tpo.marketplace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.service.UsuarioService;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
    }

    @GetMapping("/{usuarioId}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long usuarioId) throws UsuarioNotFoundException {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(usuarioId);
        if (usuario.isPresent())
            return ResponseEntity.ok(usuarioService.usuarioToUsuarioDTO(usuario.get()));
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO)
            throws UsuarioDuplicateException {
        Usuario usuario = usuarioService.createUsuario(usuarioDTO.getNombre(), usuarioDTO.getApellido(),
                usuarioDTO.getRolUsuario(), usuarioDTO.getUsername(), usuarioDTO.getPassword(), usuarioDTO.getEmail(),
                usuarioDTO.getTelefono());

        return ResponseEntity.created(URI.create("/usuarios" + usuario.getId()))
                .body(usuarioService.usuarioToUsuarioDTO(usuario));
    }
}
