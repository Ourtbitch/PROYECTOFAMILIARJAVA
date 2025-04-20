package com.ahorrofamiliar.models;

public class Usuario {
    private int id;
    private int idRol;
    private String nombre;
    private String apellido;
    private String tipoDocumento;
    private String numeroDocumento;
    private String sexo;
    private String situacion;
    private String contrasenia;

    // Constructor por defecto (sin argumentos)
    public Usuario() {
    }

    // Constructor con todos los campos (puedes elegir los que necesites al crear un nuevo usuario)
    public Usuario(int id, int idRol, String nombre, String apellido, String tipoDocumento, String numeroDocumento, String sexo, String situacion, String contrasenia) {
        this.id = id;
        this.idRol = idRol;
        this.nombre = nombre;
        this.apellido = apellido;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.sexo = sexo;
        this.situacion = situacion;
        this.contrasenia = contrasenia;
    }

    // Métodos getter y setter para cada atributo
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdRol() {
        return idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", idRol=" + idRol +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", sexo='" + sexo + '\'' +
                ", situacion='" + situacion + '\'' +
                ", contrasenia='" + contrasenia + '\'' +
                '}';
    }
}