package com.ahorrofamiliar.models;

import java.util.Date;

public class MetaAporte {
    private int id;
    private int idMeta;
    private int idUsuario;
    private double aporteEstimado;
    private double aporteReal;
    private Date fechaRegistro;
    private String situacion;

    public MetaAporte() {}

    public MetaAporte(int id, int idMeta, int idUsuario, double aporteEstimado, double aporteReal, Date fechaRegistro, String situacion) {
        this.id = id;
        this.idMeta = idMeta;
        this.idUsuario = idUsuario;
        this.aporteEstimado = aporteEstimado;
        this.aporteReal = aporteReal;
        this.fechaRegistro = fechaRegistro;
        this.situacion = situacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMeta() {
        return idMeta;
    }

    public void setIdMeta(int idMeta) {
        this.idMeta = idMeta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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
