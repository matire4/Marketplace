package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.entities.dto.UsuarioDTO;
import com.uade.tpo.marketplace.enums.Role;
import com.uade.tpo.marketplace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.repository.UsuarioRepository;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<UsuarioDTO> getUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(usuario -> this.usuarioToUsuarioDTO(usuario)).toList();
    }

    @Override
    public Optional<Usuario> getUsuarioById(Long usuarioId)
            throws UsuarioNotFoundException {
        return usuarioRepository.findById(usuarioId);
    }

    @Override
    public Optional<Usuario> getUsuarioByUsername(String username) throws UsuarioNotFoundException {
        return Optional.ofNullable(usuarioRepository.findByUsername(username));
    }

    @Override
    public Usuario createUsuario(String nombre,
            String apellido,
            Role rolUsuario,
            String username,
            String password,
            String email,
            String telefono) throws UsuarioDuplicateException {

        if (usuarioRepository.existsByUsername(username))
            throw new UsuarioDuplicateException();
        if (usuarioRepository.existsByEmail(email))
            throw new UsuarioDuplicateException();

        Usuario usuario = new Usuario(nombre, apellido, rolUsuario, username, password, email, telefono);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario updateUsuario(
            Long usuarioId,
            String nombre,
            String apellido,
            Role rolUsuario,
            String username,
            String password,
            String email,
            String telefono) throws UsuarioDuplicateException, UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());

        if (usuarioRepository.existsByUsernameAndIdNot(username, usuarioId))
            throw new UsuarioDuplicateException();
        if (usuarioRepository.existsByEmailAndIdNot(email, usuarioId))
            throw new UsuarioDuplicateException();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setRole(rolUsuario);
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setEmail(email);
        usuario.setTelefono(telefono);

        return usuario;

    }

    public void deleteUsuario(Long usuarioId) throws UsuarioNotFoundException {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException());
        usuarioRepository.delete(usuario);
    }

    @Override
    public UsuarioDTO usuarioToUsuarioDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setRolUsuario(usuario.getRole());
        usuarioDTO.setUsername(usuario.getUsername());
        usuarioDTO.setPassword(usuario.getPassword());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setTelefono(usuario.getTelefono());

        return usuarioDTO;
    }

    // @Override
    // public GestorDTO convertogestor(String username) throws
    // UsuarioNotFoundException {
    // Usuario usuario = usuarioRepository.findByUsername(username);
    // if (usuario == null) {
    // throw new UsuarioNotFoundException();
    // }
    // usuario.setRole(Role.GESTOR);
    // usuarioRepository.save(usuario);

    // return new GestorDTO(usuario.getId(), usuario.getNombre(),
    // usuario.getApellido(), usuario.getUsername());
    // }
}
