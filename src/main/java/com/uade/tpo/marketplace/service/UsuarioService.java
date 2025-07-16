package com.uade.tpo.marketplace.service;

import java.util.List;
import java.util.Optional;

import com.uade.tpo.marketplace.entities.Usuario;
import com.uade.tpo.marketplace.entities.dto.UsuarioDTO;
import com.uade.tpo.marketplace.enums.Role;
import com.uade.tpo.marketplace.exceptions.EmailDuplicateException;
import com.uade.tpo.marketplace.exceptions.TelefonoDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsernameDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsuarioDuplicateException;
import com.uade.tpo.marketplace.exceptions.UsuarioNotFoundException;

public interface UsuarioService {
        public List<UsuarioDTO> getUsuarios();

        public Optional<Usuario> getUsuarioById(Long usuarioId)
                        throws UsuarioNotFoundException;

        public Optional<Usuario> getUsuarioByUsername(String username)
                        throws UsuarioNotFoundException;

        public Usuario createUsuario(
                        String nombre,
                        String apellido,
                        Role rolUsuario,
                        String username,
                        String password,
                        String email,
                        String telefono) throws UsuarioDuplicateException;

        public UsuarioDTO updateUsuario(
                        String username,
                        String nombre,
                        String apellido,
                        Role rolUsuario,
                        String newUsername,
                        String password,
                        String email,
                        String telefono) throws UsernameDuplicateException, TelefonoDuplicateException, UsuarioNotFoundException, EmailDuplicateException;

        public void deleteUsuario(String username)
                        throws UsuarioNotFoundException;

        public UsuarioDTO usuarioToUsuarioDTO(Usuario usuario);

        public List<UsuarioDTO> getAdministradores();

        public UsuarioDTO convertToRole(String usuario, Role rol) throws UsuarioNotFoundException;
}
