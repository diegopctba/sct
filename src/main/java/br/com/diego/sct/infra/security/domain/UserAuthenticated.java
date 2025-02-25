package br.com.diego.sct.infra.security.domain;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.diego.sct.infra.security.domain.entity.Usuario;

import java.util.Collection;
import java.util.List;

public class UserAuthenticated implements UserDetails {
    private final Usuario usuario;

    public UserAuthenticated(Usuario user) {
        this.usuario = user;
    }

    @Override
    public String getUsername() {
        return usuario.getUsername();
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(() -> "read");
    }
}