package org.hospital.backendpacientes.model;


import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue
    private UUID id;

    private LocalDate fecha;

    private LocalTime hora;

    @Column(name = "paciente_id")
    private UUID pacienteId; // Puede ser null

    @Column(name = "doctor_id", nullable = false)
    private UUID doctorId;

    private String estado; // "DISPONIBLE", "AGENDADA", "CANCELADA"

    // ====== Getters y setters ======

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public UUID getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(UUID pacienteId) {
        this.pacienteId = pacienteId;
    }

    public UUID getDoctor() {
        return doctorId;
    }

    public void setDoctor(UUID doctor) {
        this.doctorId = doctor;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
