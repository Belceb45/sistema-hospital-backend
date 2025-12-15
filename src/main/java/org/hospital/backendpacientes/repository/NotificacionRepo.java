package org.hospital.backendpacientes.repository;

import org.hospital.backendpacientes.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificacionRepo extends JpaRepository<Notificacion, UUID> {
    // Traer las más recientes primero
    List<Notificacion> findByPacienteIdOrderByFechaDesc(UUID pacienteId);
}