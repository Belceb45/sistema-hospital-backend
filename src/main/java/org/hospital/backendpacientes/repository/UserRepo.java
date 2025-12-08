package org.hospital.backendpacientes.repository;
import org.hospital.backendpacientes.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByCurp(String curp);
    Optional<User> findByNumExpediente(String numExpediente);
    Optional<User> findByNombreCompleto(String nombreCompleto);
}
