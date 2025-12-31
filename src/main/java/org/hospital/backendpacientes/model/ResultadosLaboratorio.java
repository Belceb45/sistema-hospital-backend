package org.hospital.backendpacientes.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "resultados_laboratorio")
public class ResultadosLaboratorio {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID pacienteId;

    private String tipoEstudio;
    private String descripcion;
    private LocalDate fechaRealizacion;
    private String doctorSolicitante;


    private String urlArchivo;

    public ResultadosLaboratorio() {}

    public ResultadosLaboratorio(UUID pacienteId, String tipoEstudio, String descripcion, String doctorSolicitante) {
        this.pacienteId = pacienteId;
        this.tipoEstudio = tipoEstudio;
        this.descripcion = descripcion;
        this.doctorSolicitante = doctorSolicitante;
        this.fechaRealizacion = LocalDate.now();
        this.urlArchivo = "/files/resultado_ejemplo.pdf"; // Simulado
    }


    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public UUID getPacienteId() { return pacienteId; }
    public void setPacienteId(UUID pacienteId) { this.pacienteId = pacienteId; }
    public String getTipoEstudio() { return tipoEstudio; }
    public void setTipoEstudio(String tipoEstudio) { this.tipoEstudio = tipoEstudio; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public LocalDate getFechaRealizacion() { return fechaRealizacion; }
    public void setFechaRealizacion(LocalDate fechaRealizacion) { this.fechaRealizacion = fechaRealizacion; }
    public String getDoctorSolicitante() { return doctorSolicitante; }
    public void setDoctorSolicitante(String doctorSolicitante) { this.doctorSolicitante = doctorSolicitante; }
    public String getUrlArchivo() { return urlArchivo; }
    public void setUrlArchivo(String urlArchivo) { this.urlArchivo = urlArchivo; }
}