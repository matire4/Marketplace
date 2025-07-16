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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.entities.dto.UsuarioDTO;
import com.uade.tpo.marketplace.enums.Role;
import com.uade.tpo.marketplace.exceptions.EmailDuplicateException;
import com.uade.tpo.marketplace.exceptions.TelefonoDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsernameDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.service.UsuarioService;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.getUsuarios());
    }

    @GetMapping("/admins")
    public ResponseEntity<List<UsuarioDTO>> getAdministradores() {
        return ResponseEntity.ok(usuarioService.getAdministradores());
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

    @PutMapping("/{username}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@PathVariable String username, @RequestBody UsuarioDTO usuarioDTO)
            throws UsernameDuplicateException, TelefonoDuplicateException, UsuarioNotFoundException, EmailDuplicateException {
        try {
        UsuarioDTO usuario = usuarioService.updateUsuario(username, usuarioDTO.getNombre(),
                usuarioDTO.getApellido(), usuarioDTO.getRolUsuario(), usuarioDTO.getUsername(),
                usuarioDTO.getPassword(), usuarioDTO.getEmail(), usuarioDTO.getTelefono());
                return ResponseEntity.ok(usuario);
        } catch (UsuarioNotFoundException e) {
            ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
            return ResponseEntity.status(responseStatus.code()).body(null);
        } catch (UsernameDuplicateException | TelefonoDuplicateException | EmailDuplicateException e) {
            ResponseStatus responseStatus = e.getClass().getAnnotation(ResponseStatus.class);
            return ResponseEntity.status(responseStatus.code()).body(null);
        }
    }

    @PutMapping("/convertToRole/{usuario}")
    public ResponseEntity<UsuarioDTO> convertToRole(@PathVariable String usuario, @RequestParam String role) throws UsuarioNotFoundException {
        UsuarioDTO roleDTO = usuarioService.convertToRole(usuario, Role.valueOf(role));
        return ResponseEntity.ok(roleDTO);
    }
}
