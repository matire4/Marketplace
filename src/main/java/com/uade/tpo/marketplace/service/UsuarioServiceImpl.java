package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.entities.dto.UsuarioDTO;
import com.uade.tpo.marketplace.enums.Role;
import com.uade.tpo.marketplace.exceptions.EmailDuplicateException;
import com.uade.tpo.marketplace.exceptions.TelefonoDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsernameDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;
import com.uade.tpo.marketplace.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioDTO> getUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAllByRole(Role.CLIENTE);
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

    @Transactional
    @Override
    public UsuarioDTO updateUsuario(
            String username,
            String nombre,
            String apellido,
            Role rolUsuario,
            String newUsername,
            String password,
            String email,
            String telefono) throws UsernameDuplicateException, TelefonoDuplicateException, UsuarioNotFoundException, EmailDuplicateException {
        Usuario usuario = Optional.ofNullable(usuarioRepository.findByUsername(username))
                .orElseThrow(() -> new UsuarioNotFoundException());

        if (usuarioRepository.existsByUsernameAndIdNot(newUsername, usuario.getId()))
            throw new UsernameDuplicateException();
        if (usuarioRepository.existsByEmailAndIdNot(email, usuario.getId()))
            throw new EmailDuplicateException();
        if (usuarioRepository.existsByTelefonoAndIdNot(telefono, usuario.getId()))
            throw new TelefonoDuplicateException();

        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setUsername(newUsername);
        if (password != null && !password.isEmpty()){
            usuario.setPassword(passwordEncoder.encode(password));
        }
        usuario.setEmail(email);
        usuario.setTelefono(telefono);
        usuarioRepository.save(usuario);

        return this.usuarioToUsuarioDTO(usuario);

    }

    public void deleteUsuario(String username) throws UsuarioNotFoundException {
        Usuario usuario = Optional.ofNullable(usuarioRepository.findByUsername(username))
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

    @Override
    public List<UsuarioDTO> getAdministradores() {
        List<Usuario> administradores = usuarioRepository.findAllByRole(Role.ADMINISTRADOR);
        return administradores.stream().map(usuario -> this.usuarioToUsuarioDTO(usuario)).toList();
    }

    @Transactional
    @Override
    public UsuarioDTO convertToRole(String usuario, Role rol) throws UsuarioNotFoundException {
        Usuario usuarioEntity = usuarioRepository.findByUsername(usuario);
        if (usuarioEntity == null) {
            throw new UsuarioNotFoundException();
        }
        usuarioEntity.setRole(rol);
        usuarioRepository.save(usuarioEntity);
        return this.usuarioToUsuarioDTO(usuarioEntity);
    }
}
