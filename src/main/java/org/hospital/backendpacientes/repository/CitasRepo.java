package org.hospital.backendpacientes.repository;

import org.hospital.backendpacientes.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CitasRepo extends JpaRepository<Cita, UUID> {

    Optional<Cita> findCitaById(UUID citaID);
    List<Cita> findByEstado(String estado);


    List<Cita> findByPacienteId(UUID pacienteId);


    List<Cita> findByEstadoAndDoctorId(String estado, UUID doctorId);


    Optional<Cita> findFirstByDoctorIdAndEstadoOrderByFechaAscHoraAsc(UUID doctorId, String estado);

    @Query("""
        SELECT c.doctorId, MAX(c.fecha)
        FROM Cita c
        GROUP BY c.doctorId
    """)
    List<Object[]> findUltimaFechaPorDoctor();

    boolean existsByDoctorIdAndFechaAndHora(
            UUID doctorId,
            LocalDate fecha,
            LocalTime hora
    );
    void deleteByPacienteId(UUID pacienteId);
}