package com.ahorrofamiliar.dto;

import java.util.Date;

public class MetaAporteDTO {
    private int id;
    private String nombreMeta;
    private String nombreUsuario;
    private double aporteEstimado;
    private double aporteReal;
    private Date fechaRegistro;
    private String situacion;

    public MetaAporteDTO(int id, String nombreMeta, String nombreUsuario, double aporteEstimado, double aporteReal, Date fechaRegistro, String situacion) {
        this.id = id;
        this.nombreMeta = nombreMeta;
        this.nombreUsuario = nombreUsuario;
        this.aporteEstimado = aporteEstimado;
        this.aporteReal = aporteReal;
        this.fechaRegistro = fechaRegistro;
        this.situacion = situacion;
    }

    // Getters y setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreMeta() {
        return nombreMeta;
    }

    public void setNombreMeta(String nombreMeta) {
        this.nombreMeta = nombreMeta;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public double getAporteEstimado() {
        return aporteEstimado;
    }

    public void setAporteEstimado(double aporteEstimado) {
        this.aporteEstimado = aporteEstimado;
    }

    public double getAporteReal() {
        return aporteReal;
    }

    public void setAporteReal(double aporteReal) {
        this.aporteReal = aporteReal;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getSituacion() {
        return situacion;
    }

    public void setSituacion(String situacion) {
        this.situacion = situacion;
    }
}
