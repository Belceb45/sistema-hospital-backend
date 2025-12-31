package org.hospital.backendpacientes.repository;

import org.hospital.backendpacientes.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, UUID> {

    @Query("SELECT d.id FROM Doctor d WHERE d.especialidad = 'Medicina General'")
    List<UUID> findAllDoctorIds();

    // Buscar doctores por especialidad
    List<Doctor> findByEspecialidad(String especialidad);
}