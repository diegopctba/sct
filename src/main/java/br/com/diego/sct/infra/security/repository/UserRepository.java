package br.com.diego.sct.infra.security.repository;

import org.springframework.data.repository.CrudRepository;

import br.com.diego.sct.infra.security.domain.entity.Usuario;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<Usuario, UUID> {
    Optional<Usuario> findByUsername(String username);
}