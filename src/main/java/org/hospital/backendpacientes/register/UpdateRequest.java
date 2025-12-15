package org.hospital.backendpacientes.register;


import java.util.UUID;

public class UpdateRequest {
    private String numExpediente;
    private String telefono;
    private String telefono2;
    private String telefono3;
    private String direccion;
    private String fechaNacimiento;
    private UUID doctorId;

    public UUID getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(UUID doctorId) {
        this.doctorId = doctorId;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNumExpediente() {
        return numExpediente;
    }
    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }
}
