package org.hospital.backendpacientes.repository;

import org.hospital.backendpacientes.model.ResultadosLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResultadosRepo extends JpaRepository<ResultadosLaboratorio, UUID> {
    // Buscar todos los resultados de un paciente
    List<ResultadosLaboratorio> findByPacienteIdOrderByFechaRealizacionDesc(UUID pacienteId);
    void deleteByPacienteId(UUID pacienteId);
}