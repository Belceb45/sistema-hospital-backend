package org.hospital.backendpacientes.model;


import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreCompleto;
    private String curp;
    private String telefono;
    private String telefono2;
    private String telefono3;
    private String RFC;
    private String direccion;
    private String numExpediente;
    private String fechaNacimiento;

    public User() {}

    public User(String nombreCompleto, String curp,String telefono, String telefono2, String telefono3, String RFC, String direccion ) {
        this.nombreCompleto = nombreCompleto;
        this.curp = curp;
        this.telefono = telefono;
        this.telefono2 = telefono2;
        this.telefono3 = telefono3;
        this.RFC = RFC;
        this.direccion = direccion;

    }


    // Getters y setters


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
    public String getTelefono(){
        return telefono;
    }
    public void setTelefono(String telefono){
        this.telefono = telefono;
    }
    public String getTelefono2(){
        return telefono2;
    }
    public void setTelefono2(String telefono2){
        this.telefono2 = telefono2;
    }
    public String getTelefono3(){
        return telefono3;
    }
    public void setTelefono3(String telefono3){
        this.telefono3 = telefono3;
    }
    public String getRFC(){
        return RFC;
    }
    public void setRFC(String RFC){
        this.RFC = RFC;
    }
    public String getDireccion(){
        return direccion;
    }
    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

}
