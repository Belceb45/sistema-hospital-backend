package org.hospital.backendpacientes.repository;

import org.hospital.backendpacientes.model.HistorialMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface HistorialRepo extends JpaRepository<HistorialMedico, UUID> {
    // Buscamos por pacienteId, no por el ID del historial
    Optional<HistorialMedico> findByPacienteId(UUID pacienteId);
    void deleteByPacienteId(UUID pacienteId);
}