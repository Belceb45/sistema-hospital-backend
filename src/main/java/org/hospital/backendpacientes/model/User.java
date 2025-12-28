package org.hospital.backendpacientes.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid DEFAULT uuid_generate_v4()")
    private UUID id;

    private String nombreCompleto;
    private String curp;
    private String telefono;
    private String telefono2;
    private String telefono3;
    private String RFC;
    private String direccion;
    private String numExpediente;
    private String fechaNacimiento;
    //Medico asignado
    @Column(name = "doctor_id")
    private UUID doctorId;
    private Boolean afiliado;


    public User() {}

    public User(String nombreCompleto, String curp, String telefono,
                String telefono2, String telefono3, String RFC,
                String direccion,String fechaNacimiento, UUID doctorId, String numExpediente, Boolean afiliado) {
        this.nombreCompleto = nombreCompleto;
        this.curp = curp;
        this.telefono = telefono;
        this.telefono2 = telefono2;
        this.telefono3 = telefono3;
        this.RFC = RFC;
        this.direccion = direccion;
        this.fechaNacimiento = fechaNacimiento;
        this.doctorId = doctorId;
        this.numExpediente = numExpediente;
        this.afiliado = afiliado;
    }

    // ----- GETTERS & SETTERS -----
    public Boolean getAfiliado() {
        return afiliado;
    }

    public void setAfiliado(Boolean afiliado) {
        this.afiliado = afiliado;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTelefono2() {
        return telefono2;
    }

    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }

    public String getTelefono3() {
        return telefono3;
    }

    public void setTelefono3(String telefono3) {
        this.telefono3 = telefono3;
    }

    public String getRFC() {
        return RFC;
    }

    public void setRFC(String RFC) {
        this.RFC = RFC;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
    }
}
