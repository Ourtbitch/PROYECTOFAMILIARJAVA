/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.models;

import java.util.Date;

/**
 *
 * @author USER
 */
public class MetaTraslado {

    private int id;
    private int idMetaOrigen;
    private int idMetaDestino;
    private double monto;
    private Date fechaTraslado;
    private int idUsuario;
    private String nombreMetaOrigen;
    private String nombreMetaDestino;
    private String nombreUsuario;

    public MetaTraslado() {
    }

    // Constructor completo
    public MetaTraslado(int id, int idMetaOrigen, int idMetaDestino, double monto,
            Date fechaTraslado, int idUsuario) {
        this.id = id;
        this.idMetaOrigen = idMetaOrigen;
        this.idMetaDestino = idMetaDestino;
        this.monto = monto;
        this.fechaTraslado = fechaTraslado;
        this.idUsuario = idUsuario;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdMetaOrigen() {
        return idMetaOrigen;
    }

    public void setIdMetaOrigen(int idMetaOrigen) {
        this.idMetaOrigen = idMetaOrigen;
    }

    public int getIdMetaDestino() {
        return idMetaDestino;
    }

    public void setIdMetaDestino(int idMetaDestino) {
        this.idMetaDestino = idMetaDestino;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public Date getFechaTraslado() {
        return fechaTraslado;
    }

    public void setFechaTraslado(Date fechaTraslado) {
        this.fechaTraslado = fechaTraslado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreMetaOrigen() {
        return nombreMetaOrigen;
    }

    public void setNombreMetaOrigen(String nombreMetaOrigen) {
        this.nombreMetaOrigen = nombreMetaOrigen;
    }

    public String getNombreMetaDestino() {
        return nombreMetaDestino;
    }

    public void setNombreMetaDestino(String nombreMetaDestino) {
        this.nombreMetaDestino = nombreMetaDestino;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
}
