/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ahorrofamiliar.models;

import java.util.Date;

public class GestionContrasena {

    private int idGestion;
    private int idUsuario;
    private String contrasenaHash;
    private Date fechaCambio;
    private String tipoCambio;
    private String realizadoPor;

    // Constructor vacío
    public GestionContrasena() {
    }

    // Constructor con parámetros
    public GestionContrasena(int idGestion, int idUsuario, String contrasenaHash,
            Date fechaCambio, String tipoCambio, String realizadoPor) {
        this.idGestion = idGestion;
        this.idUsuario = idUsuario;
        this.contrasenaHash = contrasenaHash;
        this.fechaCambio = fechaCambio;
        this.tipoCambio = tipoCambio;
        this.realizadoPor = realizadoPor;
    }

    // Getters y Setters
    public int getIdGestion() {
        return idGestion;
    }

    public void setIdGestion(int idGestion) {
        this.idGestion = idGestion;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public Date getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(Date fechaCambio) {
        this.fechaCambio = fechaCambio;
    }

    public String getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(String tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public String getRealizadoPor() {
        return realizadoPor;
    }

    public void setRealizadoPor(String realizadoPor) {
        this.realizadoPor = realizadoPor;
    }

    @Override
    public String toString() {
        return "GestionContrasena{"
                + "idGestion=" + idGestion
                + ", idUsuario=" + idUsuario
                + ", contrasenaHash='" + contrasenaHash + '\''
                + ", fechaCambio=" + fechaCambio
                + ", tipoCambio='" + tipoCambio + '\''
                + ", realizadoPor='" + realizadoPor + '\''
                + '}';
    }
}
