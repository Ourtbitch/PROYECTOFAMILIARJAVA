/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.models;

import java.util.Date;

public class FuenteIngreso {

    private int idIngreso;
    private int idUsuario;
    private String descripcion;
    private double monto;
    private Date fechaIngreso;
    private String categoria;
    private String observaciones;

    // Constructor vacío
    public FuenteIngreso() {
    }

    // Constructor con parámetros
    public FuenteIngreso(int idIngreso, int idUsuario, String descripcion, double monto,
            Date fechaIngreso, String categoria, String observaciones) {
        this.idIngreso = idIngreso;
        this.idUsuario = idUsuario;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fechaIngreso = fechaIngreso;
        this.categoria = categoria;
        this.observaciones = observaciones;
    }

    // Getters y Setters
    public int getIdIngreso() {
        return idIngreso;
    }

    public void setIdIngreso(int idIngreso) {
        this.idIngreso = idIngreso;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "FuenteIngreso{"
                + "idIngreso=" + idIngreso
                + ", idUsuario=" + idUsuario
                + ", descripcion='" + descripcion + '\''
                + ", monto=" + monto
                + ", fechaIngreso=" + fechaIngreso
                + ", categoria='" + categoria + '\''
                + ", observaciones='" + observaciones + '\''
                + '}';
    }
}
