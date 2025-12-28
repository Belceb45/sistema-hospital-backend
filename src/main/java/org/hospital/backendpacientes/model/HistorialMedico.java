package org.hospital.backendpacientes.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "historial_medico")
public class HistorialMedico {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID pacienteId;

    private boolean diabetes;
    private boolean hipertension;
    private boolean asma;
    private boolean enfermedadesCardiacas;
    private boolean obesidad;
    @Column(length = 1000)
    private String alergias;
    @Column(length = 1000)
    private String cirugiasPrevias;
    @Column(length = 1000)
    private String antecedentesFamiliares;
    private String tipoSangre;
    private String urlExpediente;
    // Constructor vacío obligatorio
    public HistorialMedico() {}


    public String getUrlExpediente() {
        return urlExpediente;
    }
    public void setUrlExpediente(String urlExpediente) {
        this.urlExpediente = urlExpediente;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getPacienteId() { return pacienteId; }
    public void setPacienteId(UUID pacienteId) { this.pacienteId = pacienteId; }

    public boolean isDiabetes() { return diabetes; }
    public void setDiabetes(boolean diabetes) { this.diabetes = diabetes; }

    public boolean isHipertension() { return hipertension; }
    public void setHipertension(boolean hipertension) { this.hipertension = hipertension; }

    public boolean isAsma() { return asma; }
    public void setAsma(boolean asma) { this.asma = asma; }

    public boolean isEnfermedadesCardiacas() { return enfermedadesCardiacas; }
    public void setEnfermedadesCardiacas(boolean enfermedadesCardiacas) { this.enfermedadesCardiacas = enfermedadesCardiacas; }

    public boolean isObesidad() { return obesidad; }
    public void setObesidad(boolean obesidad) { this.obesidad = obesidad; }

    public String getAlergias() { return alergias; }
    public void setAlergias(String alergias) { this.alergias = alergias; }

    public String getCirugiasPrevias() { return cirugiasPrevias; }
    public void setCirugiasPrevias(String cirugiasPrevias) { this.cirugiasPrevias = cirugiasPrevias; }

    public String getAntecedentesFamiliares() { return antecedentesFamiliares; }
    public void setAntecedentesFamiliares(String antecedentesFamiliares) { this.antecedentesFamiliares = antecedentesFamiliares; }

    public String getTipoSangre() { return tipoSangre; }
    public void setTipoSangre(String tipoSangre) { this.tipoSangre = tipoSangre; }
}